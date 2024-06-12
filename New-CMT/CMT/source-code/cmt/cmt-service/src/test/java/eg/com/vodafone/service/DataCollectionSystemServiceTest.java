package eg.com.vodafone.service;

import eg.com.vodafone.model.VInput;
import eg.com.vodafone.model.VNode;
import eg.com.vodafone.model.VSystem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: basma.alkerm
 * Date: 3/27/13
 * Time: 2:38 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"applicationContext-test.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "cmtTransactionManager", defaultRollback = true)
public class DataCollectionSystemServiceTest  {

    private String sampleSystemName ="RBT";
    private String sampleSystemDescription ="This description String for testing";
    private List<String> sampleNodesNames = Arrays.asList("RBT1","RBT2","RBT3","RBT4","CSP1","CSP2");

    private VSystem testSystem1;
    private VSystem testSystem2;
    private VInput sysInput1;
    private VInput sysInput2;
    private VInput nodeInput1;
    private VInput nodeInput2;
    private VNode testNode1;
    private VNode testNode2;

    @Autowired
    private DataCollectionSystemServiceInterface systemsService ; // the class to be tested

    @Before
    public void setup() {
        testSystem2 = new VSystem();
        testSystem2.setName("Test_System");
        testSystem2.setDescription("Test System Description");

        sysInput1 = new VInput();
        sysInput1.setId("testInput1");
        sysInput1.setSystemName("Test_System");
        sysInput1.setPerNode(false);
        sysInput1.setInputName("test_input_name-$date(yyyy-mm-dd)$");
        sysInput1.setOriginalInputName("test_input_name-$date(yyyy-mm-dd)$");
        sysInput1.setHourlyName("test_input_name-$date(yyyymmddHH)$");
        sysInput1.setServer("172.18.5.48");
        sysInput1.setUser("test_user");
        sysInput1.setPassword("test_password");
        sysInput1.setPathsList(Arrays.asList("/mmf/fcd/cdr.uploaded/", "/mmf/fcd/cdr.uploaded/"));
        sysInput1.setAccessMethod("ftp_access");
        sysInput1.setType(4);

        nodeInput1 = new VInput();
        nodeInput1.setId("testInput2");
        nodeInput1.setSystemName("Test_System");
        nodeInput1.setPerNode(true);
        nodeInput1.setNodeName("testNode");
        nodeInput1.setInputName("VASPPRD4");
        nodeInput1.setServer("172.18.5.48");
        nodeInput1.setUser("VNPP");
        nodeInput1.setPassword("VNPP");
        nodeInput1.setPathsList(Arrays.asList("jdbc:oracle:thin:"));
        nodeInput1.setAccessMethod("DB_access");
        nodeInput1.setType(1);

        testNode1 = new VNode();
        testNode1.setName("testNode1");
        testNode1.setSystemName("testSystem1");
        testNode1.setDescription("Test node description");
        testNode1.setInputsList(Arrays.asList(nodeInput1));

        testSystem2.setNodesList(Arrays.asList(testNode1));

        sysInput2 = new VInput();
        sysInput2.setId("testInput3");
        sysInput2.setSystemName("Test_System");
        sysInput2.setPerNode(false);
        sysInput2.setInputName("VASPPRD4");
        sysInput2.setServer("172.18.5.48");
        sysInput2.setUser("VNPP");
        sysInput2.setPassword("VNPP");
        sysInput2.setPathsList(Arrays.asList("jdbc:oracle:thin:"));
        sysInput2.setAccessMethod("DB_access");
        sysInput2.setType(1);

        nodeInput2 =new VInput();
        nodeInput2.setId("testInput4");
        nodeInput2.setSystemName("Test_System");
        nodeInput2.setPerNode(true);
        nodeInput2.setNodeName("testNode");
        nodeInput2.setInputName("VASPPRD4");
        nodeInput2.setServer("172.18.5.48");
        nodeInput2.setUser("VNPP");
        nodeInput2.setPassword("VNPP");
        nodeInput2.setPathsList(Arrays.asList("jdbc:oracle:thin:"));
        nodeInput2.setAccessMethod("DB_access");
        nodeInput2.setType(1);

        testNode2 = new VNode();
        testNode2.setName("testNode2");
        testNode2.setSystemName("Test_System");
        testNode2.setDescription("Test node description");
        testNode2.setInputsList(Arrays.asList(nodeInput2));
    }

    @After
    public void teardown() {
    }

    @Test
    public void testListAllSystems() throws Exception {

        List<String> systems = systemsService.listAllSystems();
        Assert.assertTrue(" Test listAllSystems failed ",systems.size()>0);
    }
    @Test
    public void testGetSystem() throws Exception {
        testSystem1 = systemsService.getSystem(sampleSystemName,false );
        Assert.assertTrue("Test getSystem failed",
                ((sampleSystemName.equals(testSystem1.getName())) && (testSystem1.getNodesNames().equals(sampleNodesNames))));
    }

    @Test
    public void testUpdateSystemDescription()throws Exception{
        systemsService.updateSystemDescription(sampleSystemName, sampleSystemDescription);
        VSystem system = systemsService.getSystem(sampleSystemName,false );
        Assert.assertEquals("Test testUpdateSystemDescription failed", sampleSystemDescription,system.getDescription());
    }
    @Test
    public void testValidateSystem()throws Exception{
        systemsService.validateSystem(testSystem2);
    }
    @Test
    public void testAddNewSystem()throws Exception{
        systemsService.addNewSystem(testSystem2);
    }

    @Test
    public void testAddNewNode()throws Exception{
        testNode2.setName("testNode2");
        systemsService.addNewSystem(testSystem2);
        systemsService.addNewNode(testNode2);
    }
    @Test
    public void testGetSystemNodes ()throws Exception{
        VSystem sys=systemsService.getSystem(sampleSystemName, false);
        List<String>  actual = sys.getNodesNames();
        Assert.assertEquals("Test getSystemNodes failed", sampleNodesNames, actual);
    }


    @Test
    public void testUpdateNode()throws  Exception{
        VNode nodeToUpdate = testSystem2.getNodesList().get(0);
        nodeToUpdate.setDescription("new description");
        systemsService.updateNode(nodeToUpdate);
    }

    @Test
    public void testDeleteInput()throws Exception{
         systemsService.deleteInput(sysInput1.getId(),null,testSystem2.getName());
    }
    @Test
    public void testDeleteNode()throws Exception{
          systemsService.deleteNode(testNode2.getName(),testSystem2.getName());
    }
    @Test
    public void testDeleteSystem()throws Exception{
        testSystem1 = systemsService.getSystem("ADD_CDR_SMS",false );
          systemsService.deleteSystem(testSystem1.getName());
        systemsService.deleteSystem("ADD_CDR_SMS");
    }
    @Test
    public void testSearchSystem(){
        List<String> reslts = systemsService.searchSystems("Rbt",1,10);
        Assert.assertTrue(reslts.size() == 6);

    }
    @Test
    public void testSearchSystem2(){
        List<String> reslts = systemsService.searchSystems("Rbewet");
        Assert.assertTrue(reslts.size() == 0);

    }
    @Test
    public void testListSystemsWithIndex(){
        List<String> result = systemsService.listSystems(0,10);
        List<String> result2 = systemsService.listSystems(11,20);
        List<String> result3 = systemsService.listSystems(11,15);
        List<String> result4 = systemsService.listSystems(-10,10);
        int x =10;
    }

}
