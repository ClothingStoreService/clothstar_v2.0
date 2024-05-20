package org.store.clothstar.order.utils;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenerateOrderId {

    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final int RANDOM_NUMBER_LENGTH = 7;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // 주문생성날짜와 랜덤숫자를 이용하여, unique한 주문번호를 생성한다.
    public static Long generateOrderId() {
        String datePrefix = getDatePrefix();
        String randomDigits = generateRandomDigits();

        return Long.parseLong(datePrefix + randomDigits);
    }

    // 특정 날짜 형식으로 바꾼 현재 날짜를 얻는다.
    public static String getDatePrefix() {
        return LocalDate.now().format(DATE_TIME_FORMATTER);
    }

    // 특정 개수의 String타입 랜덤 숫자를 생성한다.
    public static String generateRandomDigits() {

        return IntStream.range(0, RANDOM_NUMBER_LENGTH)
                .mapToObj(i -> String.valueOf(SECURE_RANDOM.nextInt(10)))
                .collect(Collectors.joining());
    }
}