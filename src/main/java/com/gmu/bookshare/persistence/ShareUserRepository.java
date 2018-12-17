package com.gmu.bookshare.persistence;

import com.gmu.bookshare.entity.ShareUser;
import org.springframework.data.repository.CrudRepository;

public interface ShareUserRepository extends CrudRepository<ShareUser, Long> {

}
