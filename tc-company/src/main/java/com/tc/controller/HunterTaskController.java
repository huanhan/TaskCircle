package com.tc.controller;

import com.tc.db.entity.*;
import com.tc.db.entity.pk.HtsRecordPK;
import com.tc.db.entity.pk.HunterTaskStepPK;
import com.tc.dto.Result;
import com.tc.dto.huntertask.HTStepResult;
import com.tc.dto.task.HtsRecordDto;
import com.tc.dto.task.QueryHtsRecords;
import com.tc.dto.task.QueryHunterTask;
import com.tc.dto.task.QueryTaskInterflow;
import com.tc.dto.trans.Trans;
import com.tc.exception.DBException;
import com.tc.service.*;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping("/hunterTask")
public class HunterTaskController {


    @Autowired
    private HunterTaskService hunterTaskService;


    @Autowired
    private HtsRecordService htsRecordService;

    @Autowired
    private HunterTaskStepService hunterTaskStepService;

    @Autowired
    private UserHunterInterflowService userHunterInterflowService;

    @Autowired
    private AuditHunterTaskSerivce auditHunterTaskSerivce;

    /**
     * 根据查询条件，获取猎刃任务列表
     * @param queryHunterTask 查询条件
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value = "根据查询条件，获取猎刃任务列表")
    public Result all(@RequestBody QueryHunterTask queryHunterTask){
        Page<HunterTask> query = hunterTaskService.findByQueryHunterTask(queryHunterTask);
        return Result.init(HunterTask.toIndexAsList(query.getContent()),queryHunterTask.append(query.getTotalElements(),(long)query.getTotalPages()));
    }

    /**
     * 猎刃任务详情
     * @param id
     * @return
     */
    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "获取猎刃任务的详情信息")
    public HunterTask detail(@PathVariable("id") String id){
        HunterTask query = hunterTaskService.findOne(id);
        return HunterTask.toDetail(query);
    }


    /**
     * 获取猎刃任务步骤列表
     * @param id
     * @return
     */
    @GetMapping("/step/{id:\\d+}")
    @ApiOperation(value = "获取猎刃任务的步骤列表")
    public HTStepResult getHunterTaskSteps(@PathVariable("id") String id){
        HunterTask query = hunterTaskService.findOne(id);
        if (query == null){
            throw new DBException("猎刃任务：" + StringResourceCenter.DB_QUERY_FAILED);
        }
        int count = query.getTask().getTaskSteps().size();
        
        List<HunterTaskStep> resultSteps = new ArrayList<>();
        if (ListUtils.isNotEmpty(query.getHunterTaskSteps())){

            if (count > query.getHunterTaskSteps().size()){
                int addCount = count - query.getHunterTaskSteps().size();
                for (int i = addCount + 1; i <= count; i++) {
                    query.getHunterTaskSteps().add(new HunterTaskStep(
                            query.getId(),
                            i,
                            "猎刃未开始此步骤",
                            "猎刃未开始此步骤",
                            new Trans("NO","步骤未开始")
                    ));
                }
                
            }

            query.getHunterTaskSteps().forEach(hunterTaskStep -> {
                hunterTaskStep.setHunterTask(null);
                if (ListUtils.isNotEmpty(hunterTaskStep.getHtsRecords())){
                    hunterTaskStep.setChange(true);
                    hunterTaskStep.setHtsRecords(null);
                }
            });

            resultSteps.addAll(query.getHunterTaskSteps());
        }

        return new HTStepResult(new HunterTask(query.getId(),query.getTaskId(),query.getHunter()),resultSteps);
    }

    /**
     * 猎刃任务步骤的详情
     * @param tid
     * @param sid
     * @return
     */
    @GetMapping("/step/detail/{tid:\\d+}/{sid:\\d+}")
    @ApiOperation(value = "获取任务中单个步骤的详细情况")
    public HunterTaskStep getHunterTaskStep(@PathVariable("tid") String tid, @PathVariable("sid") Integer sid){
        HunterTaskStep query = hunterTaskStepService.findOne(new HunterTaskStepPK(tid,sid));
        if (query == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        return HunterTaskStep.toDetail(query);
    }


    @PostMapping("/hts/record/query")
    @ApiOperation(value = "根据查询条件获取猎刃任务步骤变更情况列表")
    public Result getHtsRecords(@RequestBody QueryHtsRecords queryHtsRecords){
        Page<HtsRecord> records = htsRecordService.findByQuery(queryHtsRecords);
        return Result.init(HtsRecord.toListInIndex(records.getContent()),queryHtsRecords.append(records.getTotalElements(),(long)records.getTotalPages()));
    }


    /**
     * 获取猎刃任务步骤变更情况
     * @param tid 猎刃任务编号
     * @param sid 步骤编号
     * @return
     */
    @GetMapping("/hts/record/{tid:\\d+}/{sid:\\d+}")
    @ApiOperation(value = "获取猎刃任务步骤变更情况列表")
    public Result getHtsRecords(@PathVariable("tid") String tid, @PathVariable("sid") Integer sid){
        List<HtsRecord> records = htsRecordService.findAll(tid,sid);
        return Result.init(HtsRecord.toListInIndex(records));
    }

    /**
     * 获取猎刃任务步骤变更情况详情
     * @param tid 猎刃任务编号
     * @param sid 猎刃任务步骤号
     * @param timestamp 创建的时间
     * @return
     */
    @GetMapping("/hts/record/{tid:\\d+}/{sid:\\d+}/{t}")
    @ApiOperation(value = "获取猎刃任务步骤变更情况详情")
    public HtsRecordDto getHtsRecord(@PathVariable("tid") String tid, @PathVariable("sid") Integer sid, @PathVariable("t")Timestamp timestamp){
        HtsRecord record = htsRecordService.findOne(new HtsRecordPK(tid,sid,timestamp));
        return HtsRecordDto.by(record);
    }

    /**
     * 获取指定猎刃任务，指定用户，指定猎刃的聊天内容
     * @param queryTaskInterflow
     * @return
     */
    @PostMapping("/interflow/detail/{id:\\d+}")
    @ApiOperation(value = "获取指定猎刃任务，指定用户，指定猎刃的聊天内容")
    public Result getInterflow(@RequestBody QueryTaskInterflow queryTaskInterflow, @PathVariable("id") String id){
        queryTaskInterflow.setHtId(id);
        queryTaskInterflow.setSort(new Sort(Sort.Direction.ASC,UserHunterInterflow.CREATE_TIME));
        Page<UserHunterInterflow> query = userHunterInterflowService.findByQuery(queryTaskInterflow);
        return Result.init(UserHunterInterflow.toListInIndex(query.getContent()),queryTaskInterflow
                .append(query.getTotalElements(),(long)query.getTotalPages()).clearSort());
    }

    @GetMapping("/audit/{id:\\d+}")
    @ApiOperation(value = "根据猎刃任务编号，获取任务的审核记录")
    public List<AuditHunterTask> getAuditByHunterTask(@PathVariable("id") String id){
        HunterTask hunterTask = hunterTaskService.findOne(id);
        if (hunterTask == null){
            throw new DBException("猎刃任务：" + StringResourceCenter.DB_QUERY_FAILED);
        }
        List<AuditHunterTask> query = auditHunterTaskSerivce.findByHunterTaskId(id);
        return AuditHunterTask.toIndexAsList(query);
    }
}
