package ua.pp.disik.englishroulette.desktop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.pp.disik.englishroulette.desktop.entity.Setting;

public interface SettingRepository extends
        CrudRepository<Setting, Integer>,
        PagingAndSortingRepository<Setting, Integer> {
}
