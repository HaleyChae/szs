package com.szs;

import com.opencsv.CSVReader;
import com.szs.entity.Member;
import com.szs.repository.MemberRepository;
import com.szs.security.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataLoader {
    private final MemberRepository memberRepository;
    private final EncryptUtil encryptUtil;

    @Bean
    public CommandLineRunner loadData() {
        String filePath = System.getProperty("user.dir") + "/data/member.csv";
        List<Member> members = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                members.add(
                        Member.builder()
                                .name(line[0])
                                .regNo(encryptUtil.encrypt(line[1]))
                                .build()
                );
            }
        } catch (Exception ignored) {}

        return args -> memberRepository.saveAll(members);
    }
}