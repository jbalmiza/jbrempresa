-- Una reserva ocupa exactamente una única agenda.
ALTER TABLE reserva_recursos DROP CONSTRAINT IF EXISTS uk_reserva_recurso;
ALTER TABLE reserva_recursos ADD CONSTRAINT uk_reserva_una_agenda UNIQUE (cli_id, res_id);
