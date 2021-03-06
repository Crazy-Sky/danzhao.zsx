package com.danzhao.serviceImpl;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.danzhao.bean.Examination;
import com.danzhao.bean.Examroom;
import com.danzhao.bean.ExcelBean;
import com.danzhao.bean.Student;
import com.danzhao.bean.StudentExample;
import com.danzhao.dao.StudentMapper;
import com.danzhao.dto.ExaminerGradeDto;
import com.danzhao.dto.ExamroomDto;
import com.danzhao.dto.FinishExatStuDto;
import com.danzhao.dto.ShowStuDto;
import com.danzhao.dto.StuAllInfoDto;
import com.danzhao.dto.StuInfoDto;
import com.danzhao.service.DeptService;
import com.danzhao.service.ExaminerGradeService;
import com.danzhao.service.ExamroomService;
import com.danzhao.service.ExatService;
import com.danzhao.service.ProfService;
import com.danzhao.service.StuService;
import com.danzhao.util.DatetimeUtil;
import com.danzhao.util.ExcelModeUtils;
import com.danzhao.util.ExcelUtils;
import com.danzhao.util.StringUtil;

@Service
public class StuServiceImpl implements StuService {

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    ProfService profService;

    @Autowired
    DeptService deptService;

    @Autowired
    ExamroomService examroomService;

    @Autowired
    ExaminerGradeService examinerGradeService;

    @Autowired
    ExatService exatService;

    // 导入excel
    @SuppressWarnings("static-access")
    @Override
    public int importExcelToStu(HttpServletRequest request, HttpServletResponse response) {
        // TODO excel导入
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        MultipartFile file = multipartRequest.getFile("upfile");
        if (file.isEmpty()) {
            try {
                throw new Exception("文件不存在！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        InputStream in = null;
        List<List<Object>> listob = null;
        try {
            in = file.getInputStream();
            listob = new ExcelUtils().getBankListByExcel(in, file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 该处可调用service相应方法进行数据保存到数据库中，现只对数据输出
        System.out.println("行数...：" + listob.size());
        for (int i = 0; i < listob.size(); i++) {
            List<Object> lo = listob.get(i);
            StuInfoDto stu = new StuInfoDto();
            StuInfoDto j = null;
            // System.out.println("i="+i+"\t考生号"+String.valueOf(lo.get(1)));
            try {
                j = this.selectOneDtoByStuNumber(String.valueOf(lo.get(1)));
                // System.out.println("j..............:"+j.toString());
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                System.out.println("没有新增");
            }

            stu.setExamnumber(String.valueOf(lo.get(0)));
            stu.setStunumber(String.valueOf(lo.get(1)));
            stu.setStuname(String.valueOf(lo.get(2)));
            stu.setStusex(String.valueOf(lo.get(3)));
            stu.setIdcard(String.valueOf(lo.get(4)));
            stu.setProfclass(String.valueOf(lo.get(5)));
            stu.setProfname(String.valueOf(lo.get(6)));
            stu.setErname(String.valueOf(lo.get(7)));
            stu.setTesttime(String.valueOf(lo.get(8)));
            stu.setRoleid(7);
            stu.setStustatus(0);

            // 将专业大类转化为系部id
            int deptid = deptService.selectOneByProfclass(stu.getProfclass()).getDeptid();
            stu.setDeptid(deptid);

            // 将专业名转化为专业id
            int profid = profService.selectOneByProfName(stu.getProfname()).getProfid();
            stu.setProfid(profid);

            // 将考场名转化为考场id
            int erid = examroomService.selectOneByName(stu.getErname()).getErid();
            stu.setErid(erid);

            System.out.println(stu.toString());
            Student student = new Student();
            try {
                BeanUtils.copyProperties(student, stu);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            if (j == null) {
                this.insertOneStu(student);
            } else {
                this.updateOneStu(student);
            }
        }
        return 1;
    }

    @Override
    public XSSFWorkbook exportExcel(int deptid) {
        // TODO excel导出

        // 根据条件查询数据，把数据装载到一个list中
        StuInfoDto search = new StuInfoDto();
        search.setDeptid(deptid);
        List<StuInfoDto> list = this.selectStusByDeptAndNameOrProf(search);
        for (int i = 0; i < list.size(); i++) {
            StuInfoDto stuInfoDto = list.get(i);
            stuInfoDto.setStuid(i + 1);
            list.set(i, stuInfoDto);
        }
        List<ExcelBean> excel = new ArrayList<ExcelBean>();
        Map<Integer, List<ExcelBean>> map = new LinkedHashMap<Integer, List<ExcelBean>>();
        XSSFWorkbook xssfWorkbook = null;
        // 设置标题栏
        excel.add(new ExcelBean("准考证号", "examnumber", 0));
        excel.add(new ExcelBean("考生号", "stunumber", 0));
        excel.add(new ExcelBean("考生姓名", "stuname", 0));
        excel.add(new ExcelBean("性别", "stusex", 0));
        excel.add(new ExcelBean("身份证号码", "idcard", 0));
        excel.add(new ExcelBean("专业大类", "profclass", 0));
        excel.add(new ExcelBean("第一专业", "profname", 0));
        excel.add(new ExcelBean("考场号", "ername", 0));
        excel.add(new ExcelBean("场次", "testtime", 0));

        // ------------------------------
        // 手动在mapper添加查询多表的方法 创建新dto类

        map.put(0, excel);
        String sheetName = "学生信息";
        // 调用ExcelUtil的方法
        try {
            xssfWorkbook = ExcelUtils.createExcelFile(StuInfoDto.class, list, map, sheetName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return xssfWorkbook;
    }

    // 考生签名表
    @Override
    public XSSFWorkbook exportStuSignatureExcel(int erid) {
        // TODO excel导出
        // 根据条件查询数据，把数据装载到一个list中
        List<StuInfoDto> list = this.selectStusByErId(erid);
        for (int i = 0; i < list.size(); i++) {
            StuInfoDto stuInfoDto = list.get(i);
            stuInfoDto.setStuid(i + 1);
            list.set(i, stuInfoDto);
        }
        List<ExcelBean> excel = new ArrayList<ExcelBean>();
        Map<Integer, List<ExcelBean>> map = new LinkedHashMap<Integer, List<ExcelBean>>();
        XSSFWorkbook xssfWorkbook = null;
        // 设置标题栏
        excel.add(new ExcelBean("ID", "stuid", 0));
        excel.add(new ExcelBean("姓名", "stuname", 0));
        excel.add(new ExcelBean("考生号", "stunumber", 0));
        excel.add(new ExcelBean("性别", "stusex", 0));
        excel.add(new ExcelBean("准考证号", "examnumber", 0));
        excel.add(new ExcelBean("身份证号", "idcard", 0));
        excel.add(new ExcelBean("所属系部", "deptname", 0));
        excel.add(new ExcelBean("所属专业", "profname", 0));
        excel.add(new ExcelBean("考场", "ername", 0));
        excel.add(new ExcelBean("签名", "signature", 0));
        // ------------------------------
        // 手动在mapper添加查询多表的方法 创建新dto类

        map.put(0, excel);
        String sheetName = "学生签名表";
        // 调用ExcelUtil的方法
        try {
            xssfWorkbook = ExcelUtils.createExcelFile(StuInfoDto.class, list, map, sheetName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return xssfWorkbook;
    }

    // 导出考场学生信息
    @Override
    public XSSFWorkbook exportWaitErStusInExcel(int erid) {
        // 查询此侯考场的学生
        ExamroomDto examroomDto = examroomService.selectExamroomDtoByPrimary(erid);
        List<StuInfoDto> list = new ArrayList<StuInfoDto>();
        if (examroomDto.gettestRoomList() != null) {
            examroomDto.settestRoomLists(StringUtil.StringToList(examroomDto.gettestRoomList()));
            for (String testRoomId : examroomDto.gettestRoomLists()) {
                List<StuInfoDto> tList = this.selectStusByErId(Integer.parseInt(testRoomId));
                list.addAll(tList);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            StuInfoDto stuInfoDto = list.get(i);
            stuInfoDto.setStuid(i + 1);
            stuInfoDto.setWaitRoom(examroomDto.getErname());
            if (stuInfoDto.getTesttime().equals("1")) {
                stuInfoDto.setTesttime("上午");
            } else if (stuInfoDto.getTesttime().equals("2")) {
                stuInfoDto.setTesttime("下午");
            } else if (stuInfoDto.getTesttime().equals("3")) {
                stuInfoDto.setTesttime("晚上");
            }
            list.set(i, stuInfoDto);
        }
        List<ExcelBean> excel = new ArrayList<ExcelBean>();
        Map<Integer, List<ExcelBean>> map = new LinkedHashMap<Integer, List<ExcelBean>>();
        XSSFWorkbook xssfWorkbook = null;
        // 设置标题栏
        excel.add(new ExcelBean("序号", "stuid", 0));
        excel.add(new ExcelBean("准考证号", "examnumber", 0));
        excel.add(new ExcelBean("考生号", "stunumber", 0));
        excel.add(new ExcelBean("考生姓名", "stuname", 0));
        excel.add(new ExcelBean("考生身份证号码", "idcard", 0));
        excel.add(new ExcelBean("专业大类", "profclass", 0));
        excel.add(new ExcelBean("报考专业", "profname", 0));
        excel.add(new ExcelBean("侯考场", "waitRoom", 0));
        excel.add(new ExcelBean("考试时间", "testtime", 0));
        // ------------------------------
        // 手动在mapper添加查询多表的方法 创建新dto类

        map.put(0, excel);
        String sheetName = "候考室学生信息表";
        // 调用ExcelUtil的方法
        try {
            xssfWorkbook = ExcelUtils.createExcelFile(StuInfoDto.class, list, map, sheetName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return xssfWorkbook;

    }

    // 成绩表
    @Override
    public Workbook exportStuScoreExcel(Examroom examroom,String kType, int deptid) {
		// TODO excel导出
		System.out.println("exportStuScoreExcel: " + examroom.getErid() + "<>"+ kType + "<>" + deptid);
		List<Map<String, Object>> tList = selectStuGradeByErIdAnDeptAndkType(examroom.getErid(), kType, deptid);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		int index = 1;
		if (tList.size() == 0) {
			Map<String, String> ttmap = new HashMap<String, String>();
			ttmap.put("id", "");
	        ttmap.put("prof","");
	        ttmap.put("number", "");
	        ttmap.put("name", "");
	        ttmap.put("g1","");
	        ttmap.put("g2", "");
	        ttmap.put("g3", "");
	        ttmap.put("gavg", "");
	        ttmap.put("grade","");
	        list.add(ttmap);
		}
		for (int i = 0; i < tList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			Map<String, Object> tmap = tList.get(i);
			map.put("id", index++ +"");
            map.put("prof",tmap.get("profName") + "");
            map.put("number", tmap.get("examNumber") + "");
            map.put("name", tmap.get("stuName") + "");
            float g1 = 0f,g2 = 0f,g3 =0f;
            if (tmap.get("score") != null) {
            	map.put("g1", tmap.get("score") + "");
            	g1 = (Float) tmap.get("score");
            }else {
            	map.put("g1","0");
            }
            map.put("g2", "0");
            map.put("g3", "0");
			if ((i + 1) < tList.size() && tList.get(i).get("stuId").equals(tList.get(i + 1).get("stuId"))) {		
				map.put("g2", tList.get(++i).get("score") + "");
				g2 = (Float) tList.get(i).get("score");
			}
			if ((i + 1) < tList.size() && tList.get(i).get("stuId").equals(tList.get(i + 1).get("stuId"))) {		
				map.put("g3", tList.get(++i).get("score") + "");
				g3 = (Float) tList.get(i).get("score");
			}
			map.put("gavg", (float)(Math.round((g1  + g2 + g3) / 3*10))/10 + "");
            map.put("grade", (g1  + g2 + g3) + "");
            list.add(map);
		}
        // 模板文件输入输出地址
        Workbook workbook = null;
        try {
        	String filePath;
        	String ername = "";
        	String profclass = "";
        	if (examroom.getErid() == -1) {
        		filePath = ResourceUtils.getFile("classpath:stuGradeMode2.xlsx").getPath();
        		ername = kType;
        		profclass = deptService.selectOne(deptid).getProfclass();
        	}else {
        		filePath = ResourceUtils.getFile("classpath:stuGradeMode1.xlsx").getPath();
        		ername = examroom.getErname();
        		profclass = deptService.selectOne(examroom.getDeptid()).getProfclass();
        	}
            // 开始导出
            ExcelModeUtils excelReporter = new ExcelModeUtils();
            excelReporter.setFileLoaclPath(filePath);
            excelReporter.init();
            excelReporter.export(list, ername, profclass, 5);
            workbook = excelReporter.getWorkbook();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return workbook;
	}
	
	@Override
	public List<Map<String,Object>> selectStuGradeByErIdAnDeptAndkType(int erId, String kType, int deptid){
		return studentMapper.selectStuGradeByErIdAnDeptAndkType(erId, kType, deptid);
	}

    @Override
    public int updateOneStu(Student student) {
        return studentMapper.updateByPrimaryKeySelective(student);
    }

    @Override
    public List<Student> selectAllStu() {
        return studentMapper.selectByExample(null);
    }

    @Override
    public Student selectOneStu(int stuid) {
        // TODO Auto-generated method stub
        return studentMapper.selectByPrimaryKey(stuid);
    }

    @Override
    public List<StuInfoDto> selectStusByDeptAndNameOrProf(StuInfoDto stuInfoDto) {
        return studentMapper.selectStusByDeptAndNameOrProf(stuInfoDto);
    }

    @Override
    public StuAllInfoDto selectOneStuAllInfoDto(int stuid) {
        return studentMapper.selectStuAllInfoDtoById(stuid);
    }

    @Override
    public List<StuInfoDto> selectStusByProfIdAndState(Student student) {
        return studentMapper.selectStusByProfIdAndState(student);
    }

    @Override
    public List<StuInfoDto> selectStusByErIdAndState(Student student) {
        // TODO Auto-generated method stub
        return studentMapper.selectStusByErIdAndState(student);
    }

    @Override
    public List<StuInfoDto> selectStusByShowStuDto(ShowStuDto showStuDto) {
        return studentMapper.selectStusByShowStuDto(showStuDto);
    }

    @Override
    public int deleteOne(int stuid) {
        return studentMapper.deleteByPrimaryKey(stuid);
    }

    @Override
    public StuInfoDto selectExamingStuByEr(int erid) {
        Student student = new Student();
        student.setErid(erid);
        StuInfoDto stuInfoDto = studentMapper.selectExamingStuByEr(student);
        // System.out.println(stuInfoDto);
        return stuInfoDto;
    }

    @Override
    public int updateteststarttime(int stuid) {
        Student student = studentMapper.selectByPrimaryKey(stuid);
        try {
            student.setTeststarttime(DatetimeUtil.getNowTimeOfDate());
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("获取当前时间失败");
            return -1;
        }
        return studentMapper.updateByPrimaryKeySelective(student);
    }

    @Override
    public int updatetestendtime(int stuid) {
        Student student = studentMapper.selectByPrimaryKey(stuid);
        try {
            student.setTestendtime(DatetimeUtil.getNowTimeOfDate());
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("获取当前时间失败");
            return -1;
        }
        return studentMapper.updateByPrimaryKeySelective(student);
    }

    @Override
    public int countTotleScore(int stuid) {
        List<ExaminerGradeDto> examinerGrades = examinerGradeService.selectExaminerGradesByStu(stuid);
        float totleScore = 0;
        for (ExaminerGradeDto examinerGrade : examinerGrades) {
            totleScore += examinerGrade.getScore();
        }
        totleScore /= examinerGrades.size();
        Student student = new Student();
        student.setStuid(stuid);
        student.setTotalscore(totleScore);
        return studentMapper.updateByPrimaryKeySelective(student);
    }

    @Override
    public int stuLogin(Student student, HttpSession session) {
        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andExamnumberEqualTo(student.getExamnumber());
        List<Student> students = studentMapper.selectByExample(studentExample);
        if (students.size() > 0) {
            if (students.get(0).getStupwd().equals(student.getStupwd())) {
                student = students.get(0);
                if (student.getStustatus() != 2) {
                    return -3; // 不在考试 不能登陆
                }
                // 给考生抽取考题
                student.setExatid(randExat(1, student.getDeptid()));
                student.setExatid2(randExat(2, student.getDeptid()));
                studentMapper.updateByPrimaryKeySelective(student);
                session.setAttribute("student", student);
                session.setAttribute("userCorePage", "Welcome");
                session.setAttribute("menuSelected", null);
                return 1;
            } else {
                return -2; // 密码错误
            }
        } else {
            return -1; // 找不到账户
        }
    }

    // 随机获取一个考题
    public int randExat(int exatType, int deptid) {
        Examination examination = new Examination();
        examination.setExattype(exatType);
        examination.setDeptid(deptid);
        int message = exatService.randomExat(examination, 0);
        return message;
    }

    @Override
    public StuInfoDto selectOneDtoByStuNumber(String stuNumber) {
        StudentExample studentExample = new StudentExample();
        StudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andStunumberEqualTo(stuNumber);
        List<StuInfoDto> StuInfoDtos = studentMapper.selectStudentInfoDtoByExample(studentExample);
        if (StuInfoDtos.size() > 0) {
            return StuInfoDtos.get(0);
        } else {
            return null;
        }
    }

    @Override
    public int insertOneStu(Student student) {
        // 将密码设置成准考证号
        student.setStupwd(student.getExamnumber());
        return studentMapper.insertSelective(student);
    }

    @Override
    public List<StuInfoDto> selectStusByErId(int erid) {
        return studentMapper.selectStusByErId(erid);
    }

	@Override
	public List<StuInfoDto> selectWaitCallingStuDtos(ShowStuDto showStuDto) {
		if(showStuDto.getErid() == 0 || showStuDto.getTesttime() == 0) {
			return new ArrayList<StuInfoDto>();
		}
		return studentMapper.selectWaitCallingStuDtos(showStuDto);
	}

	@Override
	public Integer queryFinishExatStuCount(FinishExatStuDto finishExatStuDto) {
		return studentMapper.queryFinishExatStuCount(finishExatStuDto);
	}

}
