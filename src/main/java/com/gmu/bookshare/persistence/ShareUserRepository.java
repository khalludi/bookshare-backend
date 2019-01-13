package com.gmu.bookshare.persistence;

import com.gmu.bookshare.entity.ShareUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShareUserRepository extends CrudRepository<ShareUser, Long> {

    List<ShareUser> findByEmail(String s);
}
