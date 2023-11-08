from PIL import Image, ImageOps
import functions_framework
from google.cloud import storage
from tempfile import NamedTemporaryFile


def is_thumbnail(name):
    return "thumbnail" in name


def image_resize(image_name, percent):  # percent of original size
    image = Image.open(image_name)
    fixed_image = ImageOps.exif_transpose(image)
    w, h = fixed_image.size
    width = int(w * percent / 100)
    height = int(h * percent / 100)
    fixed_image.thumbnail((width, height))
    return fixed_image

# Triggered by a change in a storage bucket


@functions_framework.cloud_event
def main(cloud_event):
    data = cloud_event.data

    # Bucket Name
    bucket_name = data["bucket"]
    # Source
    name = data["name"] 
    print(name)

    if is_thumbnail(name):
        return
    # Destination
    dest_file_name = "thumbnail/" + name

    storage_client = storage.Client()

    # bucket name 선언
    my_bucket = storage_client.get_bucket(bucket_name)

    # storage client 객체
    # 업로드한 파일 다운로드
    source_blob = my_bucket.get_blob(name)
    source_blob.download_to_filename(name)
    print(f"Image {name} was downloaded .")
    resized_image = image_resize(name, 50)
    # 썸네일 폴더에 업로드 하기
    with NamedTemporaryFile() as temp:
        # Extract name to the temp file
        temp_file = "".join([str(temp.name), name + '.jpeg'])
        # Save image to temp file
        resized_image.save(temp_file)
        # Uploading the temp image file to the bucket
        dest_blob = my_bucket.blob(dest_file_name)
        dest_blob.upload_from_filename(temp_file)


# requirements
"""
functions-framework == 3.*
pillow
google-cloud-storage
"""
