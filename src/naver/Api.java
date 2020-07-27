package naver;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.ApiDao;
import dao.ApiDaoImpl;
import dto.ApiDto;


public class Api {

    public static void main(String[] args) {
        String clientId = "GNwMT8S4u9kT8w1K24hu"; //애플리케이션 클라이언트 아이디값"
        String clientSecret = "oV7K_X0igF"; //애플리케이션 클라이언트 시크릿값"

        String text = null;
        try {
            text = URLEncoder.encode(Search.search(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text;    // json 결과
        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL,requestHeaders);

        System.out.println(responseBody);
        String jsonString = responseBody;
        JSONObject jObject = new JSONObject(jsonString);
        // 배열을 가져옵니다.
        JSONArray jArray = jObject.getJSONArray("items");
        
        ApiDto dto = new ApiDto();
        ApiDao dao = new ApiDaoImpl();
        
        // 배열의 모든 아이템을 출력합니다.
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            String title = obj.getString("title");
            String originallink = obj.getString("originallink");
            String link = obj.getString("link");
            String Description = obj.getString("description");
            String PubDate = obj.getString("pubDate");
            System.out.println("title(" + i + "): " + title);
            System.out.println("originallink(" + i + "): " + originallink);
            System.out.println("link(" + i + "): " + link);
            System.out.println("Description(" + i + "): " + Description);
            System.out.println("PubDate(" + i + "): " + PubDate);
            System.out.println();
            
            dto.setTitle(obj.getString("title"));
            dto.setOriginallink(obj.getString("originallink"));
            dto.setLink(obj.getString("link"));
            dto.setDescription(obj.getString("description"));
            dto.setPubDate(obj.getString("pubDate"));
            dao.insert(dto);
        }
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}