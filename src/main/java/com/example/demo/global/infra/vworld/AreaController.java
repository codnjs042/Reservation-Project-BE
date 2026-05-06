package com.example.demo.global.infra.vworld;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="Area API", description ="지역 API")
@RestController
@RequestMapping("/area")
@RequiredArgsConstructor
public class AreaController {
    private final AreaService areaService;

    @Operation(summary="지역 목록", description="지역 목록 반환")
    @GetMapping
    public ResponseEntity<List<AreaResponse>> getList(
            @Valid @ModelAttribute VWorldAreaRequest dto)
    {
        List<AreaResponse> response = areaService.getList(dto.cd());
        return ResponseEntity.ok(response);
    }
}
