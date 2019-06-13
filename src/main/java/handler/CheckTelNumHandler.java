package handler;

import com.eastrobot.robotnerve.Channel;
import com.eastrobot.robotnerve.Handler;
import com.eastrobot.robotnerve.HandlerContext;
import com.eastrobot.robotnerve.HandlerScope;
import com.eastrobot.robotnerve.annotations.handler.Priority;
import com.eastrobot.robotnerve.annotations.handler.Scope;
import com.eastrobot.robotnerve.annotations.handler.StartWith;
import com.eastrobot.robotnerve.app.HandlerHelper;
import com.eastrobot.robotnerve.events.CommandEvent;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.regex.Pattern;

@Component("checktelnum")
@Scope(HandlerScope. SESSION)
@Priority(Priority.NORMAL_PRIORITY)
public class CheckTelNumHandler implements Handler<CommandEvent> {
    private int step;
    private HandlerHelper h = HandlerHelper. getInstance();
    private static Pattern NUMBER_PATTERN = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
    @Override
    public void handle(Channel channel, CommandEvent commandEvent) {

        HandlerContext context = HandlerContext.getContext();
        if (step == 0) {
            String input = context.getInput();
            System.out.println("进入服务" + input);
            String str = "请输入手机号";
            channel.send(str);
            step++;
            System.out.println("------------------------------step"+step);
        }else if(step == 1){
            String input = context.getInput();
            System.out.println("----------------------------------------tel"+input);
            if (!"".equals(input)){
                Pattern pattern = NUMBER_PATTERN;
                Boolean result = pattern.matcher(input).matches();
                if (result == true){
                String answer = "验证通过";
                channel.send(answer);
                    destroy();
                    return;
                }else {
                String answer = "验证失败";
                channel.send(answer);
                    destroy();
                    return;
            }
            }
        }

    }

    public void destroy() {
        step = 0;
        h. ss.x();
    }
}
