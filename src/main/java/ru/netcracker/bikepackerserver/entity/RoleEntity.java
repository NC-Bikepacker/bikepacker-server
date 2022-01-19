package ru.netcracker.bikepackerserver.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_roles", schema = "public")
public class RoleEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long role_id;
        @Column(name = "role_name")
        private String role_name;

        public RoleEntity() {
        }

        public Long getRole_id() {
            return role_id;
        }

        public void setRole_id(Long role_id) {
            this.role_id = role_id;
        }

        public String getRole_name() {
            return role_name;
        }

        public void setRole_name(String role_name) {
            this.role_name = role_name;
        }

}
