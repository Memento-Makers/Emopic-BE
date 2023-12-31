package mmm.emopic.app.domain.location;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mmm.emopic.app.base.Dto.BaseResponse;
import mmm.emopic.app.domain.location.dto.response.CityResponse;
import mmm.emopic.app.domain.location.dto.response.LocationPhotoResponse;
import mmm.emopic.app.domain.location.dto.response.LocationPointResponse;
import mmm.emopic.app.domain.location.dto.response.LocationRecentResponse;
import mmm.emopic.app.domain.photo.dto.response.PageResponse;
import mmm.emopic.app.domain.photo.dto.response.PhotoInformationResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="지역 API")


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/locations")
    @Operation(summary = "전체 사진 조회", responses = {
            @ApiResponse(responseCode = "200", description = "지도 전체 사진 조회 완료", content = @Content(schema = @Schema(implementation = LocationPhotoResponse.class)))
    })
    public ResponseEntity<BaseResponse> getAllPhotosByLocationYN(){
        List<LocationPhotoResponse> response = locationService.getAllPhotos();
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "지도 전체 사진 조회 완료", response));
    }


    @GetMapping("/locations/points")
    @Operation(summary = "현재 위치에 따른 사진 정보 조회", responses = {
            @ApiResponse(responseCode = "200", description = "현재 위치에 따른 사진 정보 조회 완료", content = @Content(schema = @Schema(implementation = LocationPointResponse.class)))
    })
    public ResponseEntity<BaseResponse> getRecentPhotoByPoints(@RequestParam double latitude, @RequestParam double longitude){
        LocationPointResponse response = locationService.getCityAndCount(latitude,longitude);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "현재 위치에 따른 사진 정보 조회 완료", response));
    }

    @GetMapping("/locations/recent")
    @Operation(summary = "가장 최근 업로드한 사진 위치정보 조회", responses = {
            @ApiResponse(responseCode = "200", description = "가장 최근 업로드한 사진 위치정보 조회 완료", content = @Content(schema = @Schema(implementation = LocationRecentResponse.class)))
    })
    public ResponseEntity<BaseResponse> getRecentPhotoAndCity(){
        LocationRecentResponse response = locationService.getCityAndPhoto();
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "가장 최근 업로드한 사진 위치정보 조회 완료", response));
    }

    @GetMapping("/locations/city")
    @Operation(summary = "지역별 대표 사진 조회", responses = {
            @ApiResponse(responseCode = "200", description = "지역별 대표 사진 조회 완료", content = @Content(schema = @Schema(implementation = CityResponse.class)))
    })
    public ResponseEntity<BaseResponse> getPhotoByCity(){
        List<CityResponse> response = locationService.getPhotoByCity();
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), "지역별 대표 사진 조회 완료", response));
    }
    @GetMapping("/locations/city/photos")
    @Operation(summary = "지역의 전체 사진 조회", responses = {
            @ApiResponse(responseCode = "200", description = "지역의 전체 사진 조회 완료", content = @Content(schema = @Schema(implementation = PageResponse.class)))
    })
    public ResponseEntity<BaseResponse> getAllPhotoOfCity(@Param("city") String city, @PageableDefault(page = 0, size = 20, sort="snapped_at",direction = Sort.Direction.DESC) Pageable pageable){
        PageResponse response = locationService.getAllPhotosByCity(city,pageable);
        return ResponseEntity.ok(new BaseResponse( HttpStatus.OK.value(), String.format("%s의 전체 사진 조회 완료",city), response));
    }
}
