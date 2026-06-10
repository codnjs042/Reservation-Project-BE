-- getFamous: where status = ? order by favorites desc, id desc
CREATE INDEX idx_stores_status_favorites_id ON stores (status, favorites DESC, id DESC);

-- findTop9ByStatusOrderByCreatedAtDesc: where status = ? order by created_at desc
CREATE INDEX idx_stores_status_created_at ON stores (status, created_at DESC);

-- findWithin3km: where status in (?,?) and latitude between ... and longitude between ...
CREATE INDEX idx_stores_status_lat_lon ON stores (status, latitude, longitude);

-- findFullTimes, hasReservation: where store_id = ? and status = ? and target_date_time ...
CREATE INDEX idx_reservations_store_status_target ON reservations (store_id, status, target_date_time);

-- findFreeTable, findSafeTables: where table_id = ? and target_date_time = ? and status = ?
CREATE INDEX idx_reservations_table_target_status ON reservations (table_id, target_date_time, status);
