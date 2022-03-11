package com.example.backneodoc.email.service;

import com.example.backneodoc.email.context.AbstractEmailContext;

public interface EmailService {
    void sendMail(AbstractEmailContext email);
}