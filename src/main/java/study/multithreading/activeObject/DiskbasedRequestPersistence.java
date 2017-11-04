package study.multithreading.activeObject;

import org.apache.log4j.Logger;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 名称：
 * 功能：
 * 条件：
 * Created by wq on 2017/9/4.
 */
public class DiskbasedRequestPersistence implements RequestPersistence {
    //负责缓存文件的存储管理
    private final SectionBasedDiskStorage storage = new SectionBasedDiskStorage();
    private final Logger logger = Logger.getLogger(DiskbasedRequestPersistence.class);

    @Override
    public void store(MMSDeliverRequest request) {
        //申请缓存文件的文件名
        String[] fileNameParts = storage.apply4Filename(request);
        File file = new File(fileNameParts[0]);
        try {
            ObjectOutputStream object = new ObjectOutputStream(
                    new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            storage.decrementSectionFileCount(fileNameParts[1]);
            logger.error("缓存请求失败" + e);
        } catch (IOException e) {
            storage.decrementSectionFileCount(fileNameParts[1]);
            logger.error("缓存请求失败" + e);
        }
    }

    class SectionBasedDiskStorage {
        private Deque<String> sectionNames = new LinkedList<String>();

        /**
         * 存储子目录名->子目录下存储文件计数器
         */
        private Map<String, AtomicInteger> sectionFileCountMap = new HashMap<String, AtomicInteger>();

        private int maxFilesPerSection = 2000;

        private int maxSectionCount = 100;

        private String storageBaseDir = "D:\\tmp\\activeObject\\vpn\\";

        private final Object sectionLock = new Object();

        public String[] apply4Filename(MMSDeliverRequest request) {
            String sectionName;
            int iFileCount;
            boolean need2RemoveSection = false;
            String[] fileName = new String[2];
            //具体的Savent执行者隐藏了同步执行的控制机制，使得Proxy客户端无法知道该细节
            synchronized (sectionLock) {
                //获取当前存储的子目录名
                sectionName = this.getSectionName();
                AtomicInteger fileCount;
                fileCount = sectionFileCountMap.get(sectionName);
                iFileCount = fileCount.get();
                //当前存储子目录已满
                if (iFileCount >= maxFilesPerSection) {
                    if (sectionNames.size() >= maxSectionCount) {
                        need2RemoveSection = true;
                    }
                    sectionName = this.makeNewSectionDir();
                    fileCount = sectionFileCountMap.get(sectionName);
                }
                iFileCount = fileCount.addAndGet(1);
            }

            fileName[0] = storageBaseDir + "\\" + sectionName + "\\"
                    + new DecimalFormat("0000").format(iFileCount) + "-"
                    + request.getTimeStamp().getTime() / 1000 + "-"
                    + request.getExpiry() + ".rq";
            fileName[1] = sectionName;

            if (need2RemoveSection) {
                //删除最老的存储子目录
                String oldestSectionName = sectionNames.removeFirst();
                this.removeSection(oldestSectionName);
            }

            return fileName;
        }

        public void decrementSectionFileCount(String sectionName) {
            AtomicInteger fileCount = sectionFileCountMap.get(sectionName);
            if (null != fileCount) {
                fileCount.decrementAndGet();
            }
        }

        private boolean removeSection(String sectionName) {
            boolean result = true;

            File dir = new File(storageBaseDir + "\\" + sectionName);
            for (File file : dir.listFiles()) {
                result = result && file.delete();
            }
            result = result && dir.delete();
            return result;
        }

        private String getSectionName() {
            String sectionName;
            if (sectionNames.isEmpty()) {
                sectionName = this.makeNewSectionDir();
            } else {
                sectionName = sectionNames.getLast();
            }
            return sectionName;
        }

        private String makeNewSectionDir() {
            String sectionName;
            SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
            sectionName = sdf.format(new Date());
            File dir = new File((storageBaseDir + "\\" + sectionName));
            if (dir.mkdir()) {
                sectionNames.addLast(sectionName);
                sectionFileCountMap.put(sectionName, new AtomicInteger(0));
            } else {
                throw new RuntimeException("不能创建分区文件" + sectionName);
            }
            return sectionName;
        }
    }
}
