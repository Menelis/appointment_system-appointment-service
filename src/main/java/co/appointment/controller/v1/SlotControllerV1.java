package co.appointment.controller.v1;

import static co.appointment.shared.constant.SharedConstants.X_CORRELATION_ID;

import co.appointment.dto.SlotDTO;
import co.appointment.service.SlotService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slots")
public class SlotControllerV1 {
    private final SlotService slotService;

    @GetMapping
    public ResponseEntity<List<SlotDTO>> getSlots(@RequestHeader(X_CORRELATION_ID) final UUID xCorrelationId) {
        return ResponseEntity.ok(slotService.getAllSlot());
    }
}
