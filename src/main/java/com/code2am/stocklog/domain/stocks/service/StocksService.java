package com.code2am.stocklog.domain.stocks.service;

import com.code2am.stocklog.domain.stocks.models.dto.StocksDTO;
import com.code2am.stocklog.domain.stocks.models.entity.Stocks;
import com.code2am.stocklog.domain.stocks.repository.StocksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

@Service
public class StocksService {

    @Autowired
    private StocksRepository stockRepository;

    private static final String API_URL = "https://apis.data.go.kr/1160100/service/GetKrxListedInfoService/getItemInfo";
    private static final String SERVICE_KEY = "Bt%2BhVFL95%2FMEthp5iyplyQGh16yHgcF65moL7eFYRF%2Bqc1rnE8ANo4qPuY%2B2C2qBCQL090e0I%2Bf5GE9IdYs%2BEw%3D%3D";

    @Transactional // 메서드 실행을 하나의 트랜잭션으로 묶음
    public void fetchAndSaveStockInfo() throws IOException, SAXException, ParserConfigurationException {

        // RestTemplate을 사용하여 API에 GET 요청을 보냄
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = API_URL + "?format=xml&basDt=20240227&serviceKey=" + SERVICE_KEY;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        String responseBody = responseEntity.getBody();

        // XML 파싱을 위한 준비
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(responseBody)));

        // XML에서 각 종목 정보를 추출하여 데이터베이스에 저장
        NodeList itemList = doc.getElementsByTagName("item");
        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
            String isinCd = item.getElementsByTagName("isinCd").item(0).getTextContent();
            String itmsNm = item.getElementsByTagName("itmsNm").item(0).getTextContent();

            saveToDatabase(isinCd, itmsNm); // DB에 저장
            stockRepository.flush(); // 영속성 컨텍스트를 강제로 flush하여 변경 사항을 DB에 즉시 반영
        }
    }


    @Transactional
    public void saveToDatabase(String isinCd, String itmsNm) {
        Stocks stock = new Stocks();
        stock.setItmsNm(itmsNm);
        stock.setIsinCd(isinCd);
        stockRepository.save(stock);
    }
}