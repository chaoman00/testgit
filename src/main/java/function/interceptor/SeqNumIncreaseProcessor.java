package function.interceptor;

import com.eastrobot.robotnerve.*;
import com.eastrobot.robotnerve.app.HandlerHelper;
import com.eastrobot.robotnerve.events.CommandEvent;
import com.eastrobot.robotnerve.events.TextEvent;
import com.eastrobot.robotnerve.throughput.HandlerOutput;
import com.eastrobot.robotnerve.throughput.SendAction;
import com.eastrobot.robotnerve.throughput.TextSendAction;
import com.incesoft.ibotsdk.RobotSession;
import function.FunctionConstants;
import function.FunctionInterface;
import util.MsgUtil;
import util.StringUtil;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeqNumIncreaseProcessor implements FunctionInterface {

    private static final SeqNumIncreaseProcessor processor = new SeqNumIncreaseProcessor();


    public static SeqNumIncreaseProcessor getInstance() {
        return processor;
    }

    @Override
    public void beforeProcessor(Channel channel, Event event) {

    }

    @Override
    public void afterProcessor(Channel channel, Event event, HandlerDef handlerDef) {

    }

    @Override
    public boolean processor(Channel channel, Event event) {
        return false;
    }

    @Override
    public boolean processor(Channel channel, Event event, InterceptorChain chain) {
        if (event instanceof TextEvent && null != event
                || event instanceof CommandEvent && null != event) {
            HandlerHelper helper = HandlerHelper.getInstance();
            String userId = channel.getUser().getID();
            String question = helper.ctx().getInput();
            System.out.println(channel.getAttribute(FunctionConstants.SeqNoIncrease.INCREASE_FLAG));
            if (null != channel.getAttribute(FunctionConstants.SeqNoIncrease.INCREASE_FLAG)) {
                System.out.println("-----------------------------------------111111111111");
                if (!StringUtil.isNum(question)) {
                    return true;
                }
                Map<Integer, String> map = (Map<Integer, String>) channel.getAttribute(FunctionConstants.SeqNoIncrease.INCREASE_FLAG);
                String realQuestion = map.get(Integer.parseInt(question)) == null ? question
                        : map.get(Integer.parseInt(question));
                System.out.println("--------------------------------------realQuestion"+realQuestion);
                if (event instanceof TextEvent) {
                    TextEvent robotEvent = (TextEvent) event;
                    robotEvent.setInput(realQuestion);
                    return true;
                } else if (event instanceof CommandEvent) {
                    helper.nav.toAI(realQuestion);
                    chain.doIntercept(channel, event);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void afterProcessor(Channel channel, Event event) {
        if (event instanceof TextEvent && null != event
                || event instanceof CommandEvent && null != event) {
            HandlerHelper helper = HandlerHelper.getInstance();
            RobotSession robotSession = (RobotSession) channel.getRealSession();
            String input = helper.ctx().getInput().trim();
            String userId = channel.getUser().getID();
            String platform = helper.client.platform();

            // 序列递增上限
            String incrLimit = MsgUtil.getWithDefValue(
                    FunctionConstants.SeqNoIncrease.DEV_SEQ_NO_LIMIT, platform,
                    "100");

            HandlerContext context = HandlerContext.getContext();
            HandlerOutput aioutput = context.getHandlerOutput();
            if (aioutput != null) {
                System.out.println("---------------------------------------aioutput");
                String answer = "";
                Integer answerType = 0;
                List<SendAction> actions = aioutput.getActions();
                for (SendAction act : actions) {
                    if (act instanceof TextSendAction) {
                        answerType = (Integer) ((RobotSession) channel
                                .getRealSession())
                                .getAttribute("key_answer_type");

                        answer = ((TextSendAction) act).getText();
                    }
                }
                // 处理动态菜单(场景：默认回复、标准问)
                if (answer.contains("[activemenu]")) {
                    int index = answer.indexOf("[activemenu]");
                    int lastIndex = answer.lastIndexOf("[/activemenu]");
                    String header = answer.substring(0, index);
                    String footer = answer.substring(lastIndex + 13,
                            answer.length());

                    // 获取动态菜单的每一个菜单项并转换成数组
                    List<String> menus = StringUtil
                            .getTag(answer, "activemenu");
                    String[] menuArray = (String[]) menus
                            .toArray(new String[] {});
                    if (null != menuArray) {
                        answer = renderText(header.trim(), menuArray,
                                footer.trim(), platform, channel);
                        System.out.println("------------------------------answer"+answer);
                    }
                    if (StringUtil.isNotBlank(answer)) {
                        clearOutput(context, aioutput);
                        channel.send(answer);
                    }
                    return;
                } else {
                    if (answerType == 1) {
                        System.out.println("---------------------------------answertype=1");
                        String[] rqs = (String[]) robotSession
                                .getAttribute("key_related_question");
                        // 处理相关问
                        if (null != rqs && rqs.length > 1) {
                            String header = MsgUtil.getWithDefValue(
                                    "relatedQuestionHeader", platform, "");
                            String footer = MsgUtil.getWithDefValue(
                                    "relatedQuestionFooter", platform, "");
                            // log.debug("菜单header:"+header+"-菜单footer"+footer.trim()+"-menuArray："+rqs+"-platform:"+platform+"-robotSession:"+robotSession);
                            answer = answer
                                    + renderText(header.trim(), rqs,
                                    footer.trim(), platform, channel);
                            clearOutput(context, aioutput);
                            channel.send(answer);
                            return;
                        }
                    } else if (answerType == 11) {
                        String[] rqs = (String[]) robotSession
                                .getAttribute("key_related_question");
                        // 处理建议问
                        if (null != rqs && rqs.length > 1) {
                            String header = MsgUtil.getWithDefValue(
                                    "suggestedQuestionHeader", platform, "");
                            String footer = MsgUtil.getWithDefValue(
                                    "suggestedQuestionFooter", platform, "");
                            // log.debug("菜单header:"+header+"-菜单footer"+footer.trim()+"-menuArray："+rqs+"-platform:"+platform+"-robotSession:"+robotSession);
                            answer = renderText(header.trim(), rqs,
                                    footer.trim(), platform, channel);
                            clearOutput(context, aioutput);
                            channel.send(answer);
                            return;
                        }
                    }
                }
            }
        }
    }

    private String renderText(String header, String[] menuArray, String footer,
                              String platform, Channel channel) {
        String incrLimit = MsgUtil.getWithDefValue(
                FunctionConstants.SeqNoIncrease.DEV_SEQ_NO_LIMIT, platform, "100"
        );
        int limitNumIndex = Integer.parseInt(incrLimit);
        limitNumIndex = limitNumIndex >= menuArray.length ? limitNumIndex : menuArray.length;
        String seqStartNum = MsgUtil.getWithDefValue(
                FunctionConstants.SeqNoIncrease.DEV_SEQ_NO_START, platform, "1"
        );
        int startNumIndex = Integer.parseInt(seqStartNum);
        Map<Integer, String> map = null != (Map<Integer, String>) channel
                .getAttribute(FunctionConstants.SeqNoIncrease.INCREASE_FLAG) ? (Map<Integer, String>) channel
                .getAttribute(FunctionConstants.SeqNoIncrease.INCREASE_FLAG) : new HashMap<Integer, String>();
        int startIndex;
        int menuSize = map.size();
        if (!map.isEmpty() && (menuSize + menuArray.length) <= (limitNumIndex - startNumIndex + 1)) {
            startIndex = menuSize + startNumIndex;
        } else {
            channel.removeAttribute(FunctionConstants.SeqNoIncrease.INCREASE_FLAG);
            map = new HashMap<Integer, String>();
            startIndex = startNumIndex;
        }
        String seqRenderStyle = MsgUtil.get(FunctionConstants.SeqNoIncrease.DEV_SEQ_NO_RENDER_STYLE, platform);
        header = StringUtil.isBlank(header) ? "" : header;
        footer = StringUtil.isBlank(footer) ? "" : footer;
        StringBuffer content = new StringBuffer();
        content.append(header.trim() + "\n");
        if (menuArray != null) {
            for (int i = 0; i < menuArray.length; i++) {
                map.put(map.size() + 1, menuArray[i]);
                content.append(MessageFormat.format(seqRenderStyle, startIndex + "", menuArray[i]));
                content.append("\n");
                startIndex++;
            }
        }
        content.append(footer);
        channel.setAttribute(FunctionConstants.SeqNoIncrease.INCREASE_FLAG, map);
        return content.toString().trim();
    }

    private void clearOutput(HandlerContext context,HandlerOutput output){
        if (output != null){
            output.clear();
        }
        if (context.getHandlerOutput() != null){
            context.getHandlerOutput().clear();
        }
    }

}