package com.gmu.bookshare.service;

import com.gmu.bookshare.entity.ShareUser;
import com.gmu.bookshare.persistence.ShareUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShareUserService {

    private final ShareUserRepository shareUserRepository;

    @Autowired
    public ShareUserService(ShareUserRepository shareUserRepository) {
        this.shareUserRepository = shareUserRepository;
    }

    public ShareUser addShareUser(ShareUser user) {
        List<ShareUser> userList = shareUserRepository.findByEmail(user.getEmail());
        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            shareUserRepository.save(user);
            return user;
        }
    }

    public ShareUser getShareUserByEmail(String email) {
        List<ShareUser> foundUsers = shareUserRepository.findByEmail(email);
        if (foundUsers.size() > 0) {
            return foundUsers.get(0);
        } else {
            return null;
        }
    }

    public ShareUser getShareUserById(Long id) {
        Optional<ShareUser> shareUser = shareUserRepository.findById(id);
        return shareUser.orElse(null);
    }
}
