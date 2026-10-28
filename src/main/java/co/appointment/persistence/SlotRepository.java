package co.appointment.persistence;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface SlotRepository
        extends CrudRepository<SlotEntity, UUID>, ListPagingAndSortingRepository<SlotEntity, UUID> {}
