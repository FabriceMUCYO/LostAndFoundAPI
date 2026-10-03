package rw.ac.auca.lostandfound.controller;

import jakarta.validation.Valid;
import rw.ac.auca.lostandfound.model.Claim;
import rw.ac.auca.lostandfound.repository.ClaimRepository;
import rw.ac.auca.lostandfound.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.lostandfound.model.ActivityLog;
import rw.ac.auca.lostandfound.repository.ActivityLogRepository;
import java.time.LocalDateTime;
import rw.ac.auca.lostandfound.messaging.NotificationPublisher;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private NotificationPublisher notificationPublisher;

    @GetMapping
    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Claim> getClaimById(@PathVariable Long id) {
        return claimRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createClaim(@Valid @RequestBody Claim claim) {
        if (claim.getItem() == null || claim.getItem().getId() == null) {
            return ResponseEntity.badRequest().body("An item must be selected for this claim");
        }
        if (!itemRepository.existsById(claim.getItem().getId())) {
            return ResponseEntity.badRequest().body("Item not found");
        }
        claim.setStatus("PENDING");
        Claim saved = claimRepository.save(claim);

        activityLogRepository.save(new ActivityLog(
                null,
                "CLAIM_SUBMITTED",
                "Claim submitted by " + saved.getClaimantName() + " for item '" + saved.getItem().getName() + "'",
                LocalDateTime.now()
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClaim(@PathVariable Long id, @Valid @RequestBody Claim updatedClaim) {
        return claimRepository.findById(id)
                .map(existingClaim -> {
                    if (!isValidStatus(updatedClaim.getStatus())) {
                        return ResponseEntity.badRequest().body("Status must be PENDING, APPROVED, or REJECTED");
                    }
                    updatedClaim.setId(id);
                    Claim saved = claimRepository.save(updatedClaim);

                    activityLogRepository.save(new ActivityLog(
                            null,
                            "CLAIM_STATUS_CHANGED",
                            "Claim #" + saved.getId() + " status changed to " + saved.getStatus(),
                            LocalDateTime.now()
                    ));

                    if ("APPROVED".equalsIgnoreCase(saved.getStatus()) || "REJECTED".equalsIgnoreCase(saved.getStatus())) {
                        notificationPublisher.sendClaimStatusNotification(
                                saved.getClaimantName(),
                                saved.getItem().getName(),
                                saved.getStatus()
                        );
                    }

                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        if (!claimRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        claimRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isValidStatus(String status) {
        return "PENDING".equalsIgnoreCase(status)
                || "APPROVED".equalsIgnoreCase(status)
                || "REJECTED".equalsIgnoreCase(status);
    }
}