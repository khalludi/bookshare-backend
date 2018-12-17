package com.gmu.bookshare.service;

import com.gmu.bookshare.persistence.ShareUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShareUserService {

    private final ShareUserRepository shareUserRepository;

    @Autowired
    public ShareUserService(ShareUserRepository shareUserRepository) {
        this.shareUserRepository = shareUserRepository;
    }
}
