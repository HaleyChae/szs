package com.szs;

import com.opencsv.CSVReader;
import com.szs.entity.Member;
import com.szs.repository.MemberRepository;
import com.szs.security.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataLoader {
    private final MemberRepository memberRepository;
    private final EncryptUtil encryptUtil;

    @Bean
    public CommandLineRunner loadData() {
        String filePath = System.getProperty("user.dir") + "/data/member.csv";
        List<Member> members = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                members.add(
                        Member.builder()
                                .name(line[0])
                                .regNo(encryptUtil.encrypt(line[1]))
                                .build()
                );
            }
        } catch (Exception e) {
            log.error("Default Data Read ERROR: {}", e.getMessage(), e);
        }

        return args -> memberRepository.saveAll(members);
    }
}