package mmm.emopic.app.domain.photo.support;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Configuration
public class MetadataExtractor {

    public Optional<Date> getSnappedDate(Metadata metadata){
        if(metadata.containsDirectoryOfType(ExifIFD0Directory.class)){
            ExifIFD0Directory exif = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if(exif.containsTag(306)){
                return Optional.of(exif.getDate(306));
            }
        }
        if(metadata.containsDirectoryOfType(ExifSubIFDDirectory.class)) {
            ExifSubIFDDirectory subExif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if(subExif.containsTag(306)){
                return Optional.of(subExif.getDate(306));
            }
        }
        return Optional.empty();
    }

    public Optional<Point> getLocationPoint(Metadata metadata){
        GpsDirectory gps = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        if(metadata.containsDirectoryOfType(GpsDirectory.class)){
            if(gps.containsTag(GpsDirectory.TAG_LATITUDE) && gps.containsTag(GpsDirectory.TAG_LONGITUDE)){
                //지리적 위치 데이터를 저장하는 데 사용되는 표준 좌표계 중 하나인 WGS84(World Geodetic System 1984)를 기반으로 하는 모델
                GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
                return Optional.of(geometryFactory.createPoint(new Coordinate(gps.getGeoLocation().getLatitude(),gps.getGeoLocation().getLongitude())));
            }
        }
        return Optional.empty();
    }


    public Optional<Metadata> readMetadata(MultipartFile image){
        try{
            return Optional.of(ImageMetadataReader.readMetadata(image.getInputStream()));
        }
        catch (ImageProcessingException e) {
            System.out.println(e);
            return Optional.empty();
        } catch (IOException e) {
            System.out.println(e);
            return Optional.empty();
        }
    }
}
