create table clients (id  bigserial not null, date timestamp, status varchar(255), tag varchar(255), userid int8 not null, primary key (id))
create table globalvariables (id  bigserial not null, name varchar(255), value varchar(255), primary key (id))
create table infernalsettings (id  bigserial not null, aktive boolean, auto_bot_start boolean, auto_login boolean, client_hide boolean, client_path varchar(255), client_version varchar(255), console_hide boolean, cpu_boost boolean, dis_chest boolean, groups int4, leader_cpu_boost boolean, leader_hide boolean, leader_render_disable boolean, level_to_beginner_bot int4, lol_height int4, lol_width int4, max_be int4, max_level int4, open_chest boolean, open_hex_tech boolean, play_time int4, prio int4, queue_close_value int4, queuer_auto_close boolean, ram_manager boolean, ram_max int4, ram_min int4, region int4, render_disable boolean, replace_config boolean, sets varchar(255), sleep_time int4, soft_end_default boolean, soft_end_value int4, surrender boolean, time_span int4, time_until_check boolean, time_until_reboot varchar(255), timeout_champ int4, timeout_end_of_game int4, timeout_in_game int4, timeout_in_gameff int4, timeout_load_game int4, timeout_lobby int4, timeout_login int4, timeout_mastery int4, wildcard varchar(255), win_reboot boolean, win_shutdown boolean, userid int8 not null, primary key (id))
create table lolaccounts (id  bigserial not null, account varchar(255), account_status varchar(255), active boolean not null, assigned_to varchar(255), be int4, info varchar(255), level int4, max_be int4, max_level int4, password varchar(255), play_time int4, priority int4, region varchar(255), sleep_time int4, summoner varchar(255), xp int4, userid int8 not null, primary key (id))
create table privileges (id  bigserial not null, name varchar(255), primary key (id))
create table proxies (id  bigserial not null, handle varchar(255) not null, proxy_host varchar(255) not null, proxy_password varchar(255) not null, proxy_port varchar(255) not null, proxy_type int4 not null, proxy_user varchar(255) not null, primary key (id))
create table queuerlolaccounts (id  bigserial not null, account varchar(255), be int4, champ varchar(255), lane int4, level int4, lpq boolean, max_level int4, xp int4, xp_cap int4, queuerid int8 not null, primary key (id))
create table queuers (id  bigserial not null, after_game int4, defeat_games int4, played_games int4, queuer varchar(255), soft_end boolean, win_games int4, clientid int8 not null, primary key (id))
create table roles (id  bigserial not null, name varchar(255) not null, primary key (id))
create table roles_privileges (roleid int8 not null, privilegeid int8 not null)
create table users (id  bigserial not null, email varchar(255), enabled boolean not null, password varchar(255), infernalsettingsid int8, primary key (id))
create table users_roles (userid int8 not null, roleid int8 not null)
create table verificationtokens (id  bigserial not null, expiry_date date, token varchar(255), user_id int8 not null, primary key (id))
alter table globalvariables add constraint UK_191uajg0m23hk3qlj82nl2omp unique (name)
alter table infernalsettings add constraint UK_r4s79o6u311701tbjs26g6jog unique (userid)
alter table lolaccounts add constraint UKpag1bsitoqd1uwp5l8kfqwy5g unique (account, region)
alter table roles add constraint UK_ofx66keruapi6vyqpv6f2or37 unique (name)
alter table users add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email)
alter table clients add constraint FKkyi3xo97w0s7v2gwtncni6jhs foreign key (userid) references users
alter table infernalsettings add constraint FK2ukbnh8ut7x0l1uu6yscypg0h foreign key (userid) references users
alter table lolaccounts add constraint FKsxn97cr9r4gjoeukt9g2b9crq foreign key (userid) references users
alter table queuerlolaccounts add constraint FKkjaomljiwnbf6ureq8f3td099 foreign key (queuerid) references queuers
alter table queuers add constraint FKbobxj6s5hytlehgvanj0qaa0v foreign key (clientid) references clients
alter table roles_privileges add constraint FKjjk3p4xnbwtat3ud0o4o086pn foreign key (privilegeid) references privileges
alter table roles_privileges add constraint FK6mgdocrni31hn4k63e5k52g6s foreign key (roleid) references roles
alter table users add constraint FK18jpw5ke1f1hh910xiqdxoc2k foreign key (infernalsettingsid) references infernalsettings
alter table users_roles add constraint FKalw2yaqhsip58rjm076ngh5v7 foreign key (roleid) references roles
alter table users_roles add constraint FK60halrubbu6j1200pijr8lqpr foreign key (userid) references users
alter table verificationtokens add constraint FKg07mxtlf2s0y3b3ulro1y6hp7 foreign key (user_id) references users
