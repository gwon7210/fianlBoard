package com.simplify.sample.db.service;

import com.simplify.sample.db.dto.*;
import com.simplify.sample.db.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestService {

    @Resource
    TestMapper testMapper;

    public List<Test> getAll() throws Exception{
        return testMapper.getAll();
    }

    public void insertMain(memberVO map) throws Exception{
        testMapper.insertMainList(map);
    }

    public int checkUserInfo(memberVO map) throws Exception{
        return testMapper.checkUserInfo(map);
    }

    public void insertContent(contentVO con) throws Exception{
        testMapper.insertContent(con);
    }

    public List<contentVO> getAllContent() throws Exception{
        return testMapper.getAllContent();
    }

    public List<allcontentVO> getContent() throws Exception{
        return testMapper.getContent();
    }

    public contentVO getContentDetail(contentVO con) throws Exception{
        return testMapper.getContentDetail(con);
    }

    public List<contentVO> findContentById(contentVO con) throws Exception{
        return testMapper.findContentById(con);
    }

    public void updateContent(contentVO con) throws Exception{
        testMapper.updateContent(con);
    }

    public void insertCommnet(commentVO con) throws Exception{
        testMapper.insertCommnet(con);
    }

    public List<commentVO> findCommentsByBoardId(contentVO con) throws Exception {
        return testMapper.findCommentsByBoardId(con);
    }

    public void deleteContentById(int con) throws Exception {
        testMapper.deleteContentById(con);
    }

    public List<contentVO> searchContentByContentWord(String word) throws Exception {
        return testMapper.searchContentByContentWord(word);
    }

//    public String compareWriterAndSessionUser(int sessionId) throws Exception{
//        return testMapper.compareWriterAndSessionUser(sessionId);
//    }
    public contentVO compareWriterAndSessionUser(int id) throws Exception{
        return testMapper.compareWriterAndSessionUser(id);
    }
    public void initalizeId() throws Exception{
        testMapper.initalizeId();
    }
    public void setUpZero() throws Exception{
        testMapper.setUpZero();
    }

    public List<allcontentVO> testGetContent(pageNumber pageNumber) throws Exception{
        return testMapper.testGetContent(pageNumber);
    }
    public int count() throws Exception {
        return testMapper.count();
    }
//    public List<contentVO> listPage(Map<String, String> map) throws Exception{
//        return testMapper.listPage(map);
//    }
    public List listPage(int displayPost, int postNum) throws Exception {

        HashMap data = new HashMap();

        data.put("displayPost", displayPost);
        data.put("postNum", postNum);

        return testMapper.listPage(data);
    }
    public int findTotalCount(HashMap map) throws Exception {

        return testMapper.findTotalCount(map);
    }
    public List<contentVO> selectKeyWord(searchWord searchword) throws Exception {
        return testMapper.selectKeyWord(searchword);
    }

}
