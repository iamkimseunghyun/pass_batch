package io.laaf.passbatch.repository.packaze;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class PackageRepositoryTest {

    @Autowired
    private PackageRepository packageRepository;

    @Test
    void test_save() {
        // given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디 챌린지 PT 12주");
        packageEntity.setPeriod(84);

        // when
        packageRepository.save(packageEntity);


        // then
        assertNotNull(packageEntity.getPackageSeq());

    }

    @Test
    void test_findByCreatedAtAfter() {
        // given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        PackageEntity packageEntity0 = new PackageEntity();
        packageEntity0.setPackageName("학생 전용 3개월");
        packageEntity0.setPeriod(90);
        packageRepository.save(packageEntity0);

        PackageEntity packageEntity1 = new PackageEntity();
        packageEntity1.setPackageName("학생 전용 6개월");
        packageEntity1.setPeriod(180);
        packageRepository.save(packageEntity1);

        // when
        final List<PackageEntity> packageEntityList = packageRepository.findByCreatedAtAfter(dateTime, PageRequest.of(0, 1, Sort.by("packageSeq").descending()));

        // then
        assertEquals(1, packageEntityList.size());
        assertEquals(packageEntity1.getPackageSeq(), packageEntityList.get(0).getPackageSeq());
    }

    @Test
    void test_updateCountAndPeriod() {
        // given
        PackageEntity packageEntity0 = new PackageEntity();
        packageEntity0.setPackageName("학생 전용 4개월");
        packageEntity0.setPeriod(90);
        packageRepository.save(packageEntity0);

        // when
        int updatedCount = packageRepository.updateCountAndPeriod(packageEntity0.getPackageSeq(), 30, 120);
        final PackageEntity updatedPackageEntity = packageRepository.findById(packageEntity0.getPackageSeq()).get();

        // then
        assertEquals(1, updatedCount);
        assertEquals(30, updatedPackageEntity.getCount());
        assertEquals(120, updatedPackageEntity.getPeriod());
    }

    @Test
    void test_delete() {
        // given
        PackageEntity packageEntity0 = new PackageEntity();
        packageEntity0.setPackageName("제거할 이용권");
        packageEntity0.setCount(1);
        PackageEntity saved = packageRepository.save(packageEntity0);

        // when
        packageRepository.deleteById(saved.getPackageSeq());


        // then
        assertTrue(packageRepository.findById(saved.getPackageSeq()).isEmpty());

    }

}