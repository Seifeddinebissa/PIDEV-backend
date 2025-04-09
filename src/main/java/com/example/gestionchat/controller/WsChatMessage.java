package com.example.gestionchat.controller;

public class WsChatMessage {
        private String sender;
        private String content;
        private WsChatMessageType type;
        private String role;  // Rôle de l'utilisateur : "ETUDIANT" ou "ADMINISTRATION"

        public WsChatMessage() {
        }

        public WsChatMessage(String sender, String content, WsChatMessageType type, String role) {
                this.sender = sender;
                this.content = content;
                this.type = type;
                this.role = role;
        }

        public String getSender() {
                return sender;
        }

        public void setSender(String sender) {
                this.sender = sender;
        }

        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
        }

        public WsChatMessageType getType() {
                return type;
        }

        public void setType(WsChatMessageType type) {
                this.type = type;
        }

        public String getRole() {
                return role;
        }

        public void setRole(String role) {
                this.role = role;
        }

        public static WsChatMessageBuilder builder() {
                return new WsChatMessageBuilder();
        }

        public static class WsChatMessageBuilder {
                private String sender;
                private String content;
                private WsChatMessageType type;
                private String role;

                public WsChatMessageBuilder sender(String sender) {
                        this.sender = sender;
                        return this;
                }

                public WsChatMessageBuilder content(String content) {
                        this.content = content;
                        return this;
                }

                public WsChatMessageBuilder type(WsChatMessageType type) {
                        this.type = type;
                        return this;
                }

                public WsChatMessageBuilder role(String role) {
                        this.role = role;
                        return this;
                }

                public WsChatMessage build() {
                        return new WsChatMessage(sender, content, type, role);
                }
        }
}