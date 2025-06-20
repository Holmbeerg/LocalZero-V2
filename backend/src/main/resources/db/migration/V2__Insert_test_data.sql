INSERT INTO users (email, password_hash, name, location, created_at)
VALUES
-- Community Organizers
('nas@queens.com', '$2a$10$LaPBVGdF0aH2Gx.aWjUbieeZBoZncGCrVHcXWxlvl8npUSGTW1H7C', 'Nas', 'MOLLEVANGEN',
 '2025-01-15 10:00:00+01'),
('erik.eco@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Erik Nilsson', 'VASTRA_HAMNEN',
 '2025-01-20 14:30:00+01'),
('maja.sustain@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Maja Lindqvist',
 'MOLLEVANGEN', '2025-02-01 09:15:00+01'),

-- Residents
('lars.hansson@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Lars Hansson', 'CENTRUM',
 '2025-02-10 16:45:00+01'),
('sofia.berg@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sofia Berg', 'LIMHAMN',
 '2025-02-15 11:20:00+01'),
('peter.storm@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Peter Ström', 'ROSENGARD',
 '2025-02-20 13:10:00+01'),
('emma.vik@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Emma Vik', 'HYLLIE',
 '2025-03-01 08:30:00+01'),
('nils.oak@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Nils Ek', 'MOLLEVANGEN',
 '2025-03-05 12:00:00+01'),
('lisa.sun@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Lisa Sol', 'DAVIDSHALL',
 '2025-03-10 15:25:00+01'),
('johan.wind@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Johan Vind', 'AUGUSTENBORG',
 '2025-03-15 10:40:00+01'),
('karin.leaf@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Karin Löv', 'KULLADAL',
 '2025-03-20 14:15:00+01'),
('anders.rain@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Anders Regn', 'LORENSBORG',
 '2025-03-25 09:50:00+01');

INSERT INTO user_roles (role_id, user_id)
VALUES
-- Community Organizers (users 1, 2, 3)
(2, 1),
(2, 2),
(2, 3),
-- All users are also residents
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10),
(1, 11),
(1, 12);

INSERT INTO eco_actions (user_id, eco_action_type_id, action_date, created_at)
VALUES (1, 1, '2024-06-19', '2025-06-19 08:00:00+02'),
       (1, 10, '2025-06-19', '2025-06-19 12:30:00+02'),
       (2, 3, '2025-06-18', '2025-06-18 07:45:00+02'),
       (2, 8, '2025-06-18', '2025-06-18 18:20:00+02'),
       (3, 5, '2025-06-17', '2025-06-17 09:00:00+02'),
       (4, 9, '2025-06-17', '2025-06-17 13:15:00+02'),
       (5, 2, '2025-06-16', '2025-06-16 08:30:00+02'),
       (6, 7, '2025-06-16', '2025-06-16 19:00:00+02'),
       (7, 4, '2025-06-15', '2025-06-15 07:20:00+02'),
       (8, 10, '2025-06-15', '2025-06-15 16:45:00+02'),

       (1, 6, '2025-06-10', '2025-06-10 14:00:00+02'),
       (2, 4, '2025-06-08', '2025-06-08 11:30:00+02'),
       (3, 7, '2025-06-05', '2025-06-05 15:20:00+02'),
       (4, 1, '2025-06-03', '2025-06-03 08:15:00+02'),
       (5, 5, '2025-06-01', '2025-06-01 17:30:00+02');

INSERT INTO initiatives (title, description, location, category, is_public, creator_id, start_date, end_date,
                         created_at)
VALUES ('Centrum Tool Library',
        'Share tools and equipment with neighbors. From drills to garden tools - borrow what you need!',
        'CENTRUM', 'TOOL_SHARING', true, 1, '2025-03-01', NULL, '2025-02-25 10:00:00+01'),

       ('Västra Hamnen Seed Swap',
        'Monthly seed and plant exchange. Bring seeds from your garden and take home something new!',
        'VASTRA_HAMNEN', 'FOOD_SWAP', true, 2, '2025-04-15', '2025-10-15', '2025-04-01 14:30:00+02'),

       ('Möllevången Community Garden',
        'Grow vegetables together in our shared garden space. Perfect for apartment dwellers!',
        'MOLLEVANGEN', 'COMMUNITY_GARDENING', true, 3, '2025-03-15', '2025-11-30',
        '2025-03-01 09:15:00+01'),

       ('Rosengård Recycling Drive',
        'Monthly electronics and textile recycling event. Help keep electronics out of landfills!', 'ROSENGARD',
        'RECYCLING_DRIVE', true, 1, '2025-05-01', NULL, '2025-04-20 11:00:00+02'),

       ('Hyllie Carpool Network', 'Connect with neighbors for shared rides to work, shopping, and events around Malmö.',
        'HYLLIE', 'RIDE_SHARING', true, 2, '2025-04-01', NULL, '2025-03-25 16:20:00+01'),

       ('Limhamn Beach Cleanup Tools',
        'Borrow cleanup supplies for beach and park maintenance. Keep our coast beautiful!', 'LIMHAMN',
        'TOOL_SHARING', true, 3, '2025-05-15', NULL, '2025-05-10 13:45:00+02');

INSERT INTO initiative_members (initiative_id, user_id, joined_at) VALUES
-- Centrum Tool Library members
(1, 1, '2025-03-01 10:30:00'),
(1, 4, '2025-03-05 14:20:00'),
(1, 9, '2025-03-12 11:15:00'),
(1, 11, '2025-03-18 16:40:00'),

-- Västra Hamnen Seed Swap members
(2, 2, '2025-04-15 14:30:00'),
(2, 7, '2025-04-20 09:25:00'),
(2, 10, '2025-04-25 15:10:00'),

-- Möllevången Community Garden members
(3, 3, '2025-03-15 09:15:00'),
(3, 8, '2025-03-20 12:30:00'),
(3, 11, '2025-03-25 10:45:00'),
(3, 5, '2025-04-02 14:15:00'),

-- Rosengård Recycling Drive members
(4, 1, '2025-05-01 11:00:00'),
(4, 6, '2025-05-03 13:20:00'),
(4, 12, '2025-05-08 16:35:00'),

-- Hyllie Carpool Network members
(5, 2, '2025-04-01 16:20:00'),
(5, 7, '2025-04-05 08:45:00'),
(5, 9, '2025-04-10 12:10:00'),
(5, 4, '2025-04-15 17:30:00'),

-- Limhamn Beach Cleanup Tools members
(6, 3, '2025-05-15 13:45:00'),
(6, 5, '2025-05-18 10:20:00'),
(6, 8, '2025-05-22 15:55:00');

-- Insert posts for initiatives
INSERT INTO posts (initiative_id, author_id, text, created_at) VALUES
-- Tool Library posts
(1, 1, 'Welcome to the Centrum Tool Library! We now have 15 different tools available for borrowing. Check the list in our shared document.', '2024-03-02 10:00:00+01'),
(1, 4, 'Just borrowed the electric drill - works perfectly! Thanks for organizing this Anna. Returned it clean and charged.', '2024-03-15 14:30:00+01'),
(1, 9, 'Does anyone have a tile cutter? Planning some bathroom renovations and would love to borrow one if available.', '2024-04-22 16:45:00+02'),

-- Seed Swap posts
(2, 2, 'Excited for our first seed swap this weekend! I''ll bring tomato, basil, and sunflower seeds. What is everyone else bringing?', '2024-04-13 12:20:00+02'),
(2, 7, 'Great event yesterday! Got some amazing heirloom cucumber seeds. Already started germinating them on my windowsill.', '2024-04-16 08:45:00+02'),
(2, 10, 'Next swap is May 15th. I''ll have mint, oregano, and marigold seeds available. Looking for any berry plant starts!', '2024-05-01 19:15:00+02'),

-- Community Garden posts
(3, 3, 'Our spring planting day was amazing! 12 people showed up and we got all the beds prepared. Next work day is this Saturday.', '2024-03-25 17:30:00+01'),
(3, 8, 'The lettuce and radishes are already sprouting! Can''t wait to see our first harvest. Thanks everyone for the hard work.', '2024-04-08 11:20:00+02'),
(3, 11, 'Reminder: watering schedule is posted on the shed. Please check off your name when you water. The plants look great!', '2024-05-12 13:40:00+02'),

-- Recycling Drive posts
(4, 1, 'First recycling drive collected 50kg of electronics! Great turnout. Next event is June 1st - spread the word to your neighbors.', '2024-05-05 15:10:00+02'),
(4, 6, 'Brought my old laptop and some cables. So convenient to have this service in the neighborhood. When is the next textile collection?', '2024-05-08 12:25:00+02'),

-- Carpool posts
(5, 2, 'Looking for carpool partners to IKEA this weekend. I have space for 3 people. Leaving Saturday morning around 10am.', '2024-04-18 20:30:00+02'),
(5, 7, 'Regular carpool to Malmö C every weekday at 7:30am. Have one spot available. Message me if interested!', '2024-05-15 07:45:00+02'),

-- Beach Cleanup posts
(6, 3, 'Beach cleanup tools are now available! Grabbers, gloves, and bags stored in the blue container by the marina office.', '2024-05-16 14:20:00+02'),
(6, 5, 'Used the cleanup kit yesterday for a family walk. Collected a full bag of litter. The tools made it easy and fun for the kids too!', '2024-05-25 16:35:00+02');

-- Insert comments on posts
INSERT INTO comments (post_id, author_id, text, created_at) VALUES
-- Comments on tool library posts
(1, 4, 'This is such a great idea! How do we reserve tools in advance?', '2024-03-02 12:15:00+01'),
(1, 9, 'Perfect timing - I need to hang some shelves next week. Will check out the drill!', '2024-03-02 14:45:00+01'),
(2, 1, 'So glad it worked well for you Lars! That''s exactly what we hoped for.', '2024-03-15 16:20:00+01'),
(3, 1, 'Not yet, but I''ll ask around. Maybe we can add one to our collection if there''s demand.', '2024-04-22 18:30:00+02'),
(3, 11, 'I might be able to lend mine occasionally. Send me a message with your timeline.', '2024-04-23 09:15:00+02'),

-- Comments on seed swap posts
(4, 7, 'I''ll bring pepper and herb seeds! Maybe some flower seeds too.', '2024-04-13 15:30:00+02'),
(4, 10, 'Planning to bring bean and pea seeds. Can''t wait for the swap!', '2024-04-13 17:45:00+02'),
(5, 2, 'Wonderful! Those cucumber varieties are really special. Happy growing!', '2024-04-16 10:20:00+02'),
(6, 7, 'I have some strawberry runners that might work! Will bring them to the swap.', '2024-05-01 21:30:00+02'),

-- Comments on garden posts
(7, 8, 'Such a productive day! The beds look amazing. See everyone Saturday!', '2024-03-25 19:45:00+01'),
(7, 11, 'Great organization Maja. The tool sharing system is working perfectly too.', '2024-03-25 20:10:00+01'),
(8, 3, 'Nature is amazing! Wait until you see the tomatoes - they''re going to be incredible.', '2024-04-08 13:15:00+02'),
(9, 8, 'Thanks for the reminder Karin! I watered yesterday evening and checked off my slot.', '2024-05-12 15:20:00+02'),

-- Comments on other posts
(10, 6, 'Amazing results! This community really cares about the environment.', '2024-05-05 17:25:00+02'),
(11, 1, 'Textile collection is planned for July. Will post details once we confirm the date.', '2024-05-08 14:40:00+02'),
(12, 9, 'I''d love to join if you still have space! I need some new curtains.', '2024-04-18 21:45:00+02'),
(13, 5, 'That sounds perfect for my commute! Will send you a message tomorrow.', '2024-05-15 08:30:00+02'),
(15, 8, 'What a great way to combine exercise with helping the environment!', '2024-05-25 18:20:00+02');

INSERT INTO likes (post_id, user_id, liked_at) VALUES
-- Likes on tool library posts
( 1, 4, '2024-03-02 12:20:00+01'),
( 1, 9, '2024-03-02 14:50:00+01'),
( 2, 1, '2024-03-15 16:25:00+01'),
( 3, 1, '2024-04-22 18:35:00+02'),
( 3, 11, '2024-04-23 09:20:00+02');