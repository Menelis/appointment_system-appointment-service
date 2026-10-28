CREATE TABLE slots
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version BIGINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    slot_start VARCHAR(20) NOT NULL,
    slot_end VARCHAR(20) NOT NULL
);

CREATE TABLE appointments
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version BIGINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_by UUID,
    customer_id UUID NOT NULL,
    branch_id UUID NOT NULL,
    appointment_date DATE NOT NULL,
    slot_id UUID REFERENCES slots(id),
    description VARCHAR(500),
    reference_no varchar(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    cancellation_reason VARCHAR(500)
);