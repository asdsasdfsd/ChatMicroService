package cn.xeblog.xechat.aop;

import cn.xeblog.xechat.domain.dto.ChatRecordDTO;
import cn.xeblog.xechat.domain.vo.MessageVO;
import cn.xeblog.xechat.enums.MessageTypeEnum;
import cn.xeblog.xechat.service.ChatRecordService;
import cn.xeblog.xechat.utils.SensitiveWordUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@Aspect
@Component
@Slf4j
public class ChatRecordAspect {

    @Resource
    private ChatRecordService chatRecordService;

    @Pointcut("@annotation(cn.xeblog.xechat.annotation.ChatRecord)")
    public void chatRecordPointcut() {
    }

    @Before("chatRecordPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        log.debug("before -> {}", joinPoint);

        MessageVO messageVO = null;
        Object[] args = joinPoint.getArgs();
        for (Object obj : args) {
            if (obj instanceof MessageVO) {
                messageVO = (MessageVO) obj;
                break;
            }
        }

        Assert.notNull(messageVO, "The method must take the MessageVO class or its subclass as a parameter");

        if (messageVO.getType() == MessageTypeEnum.USER) {
            // 对于User类型的消息做敏感词处理
            messageVO.setMessage(SensitiveWordUtils.loveChina(messageVO.getMessage()));
        }

        log.debug("add chat record -> {}", messageVO);
        chatRecordService.addRecord(ChatRecordDTO.toChatRecordDTO(messageVO));
    }

}
