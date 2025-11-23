package co.appointment.controller;

import co.appointment.dto.SlotDTO;
import co.appointment.service.SlotService;
import co.appointment.shared.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slot")
public class SlotControllerV1 {
    private final SlotService slotService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SlotDTO>>> getSlots() {
        return ResponseEntity.ok(new ApiResponse<>(slotService.getAllSlot()));
    }
}
