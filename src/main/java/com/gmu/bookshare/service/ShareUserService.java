package com.gmu.bookshare.service;

import com.gmu.bookshare.entity.ShareUser;
import com.gmu.bookshare.persistence.ShareUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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

    public ShareUser getShareUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null
                && auth.getPrincipal() != null
                && auth.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) auth.getPrincipal()).getUsername();
            ShareUser user = getShareUserByEmail(username);
            if (user == null) {
                ShareUser newUser = new ShareUser(username, "default", new HashSet<>(), new HashSet<>());
                user = addShareUser(newUser);
            }
            return user;
        } else {
            return new ShareUser();
        }

    }
}
