ALTER TABLE chat_message_reaction MODIFY COLUMN reaction_type ENUM('SMILE', 'SURPRISE', 'CRY', 'NEUTRAL', 'ANGRY') NOT NULL;
