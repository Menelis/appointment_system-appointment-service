package co.appointment.controller.v1;

import co.appointment.dto.SlotDTO;
import co.appointment.service.SlotService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slot")
public class SlotControllerV1 {
    private final SlotService slotService;

    @GetMapping
    public ResponseEntity<List<SlotDTO>> getSlots() {
        return ResponseEntity.ok(slotService.getAllSlot());
    }
}
