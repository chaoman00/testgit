package function.interceptor;

import com.eastrobot.robotnerve.Channel;
import com.eastrobot.robotnerve.Event;
import com.eastrobot.robotnerve.Interceptor;
import com.eastrobot.robotnerve.InterceptorChain;
import com.eastrobot.robotnerve.annotations.handler.Priority;
import com.eastrobot.robotnerve.app.HandlerHelper;
import org.springframework.stereotype.Component;

@Component
@Priority(Priority.NORMAL_PRIORITY - 2)
public class SeqNumIncreaseInterceptor implements Interceptor {

    private SeqNumIncreaseProcessor processor = SeqNumIncreaseProcessor.getInstance();
    private static final HandlerHelper helper = HandlerHelper.getInstance();
    @Override
    public void intercept(Channel channel, Event event, InterceptorChain chain) {
        System.out.println("--------------------------------------------SEQ");
        boolean isContinue = Boolean.TRUE;
        isContinue = processor.processor(channel, event, chain);
        if (isContinue) {
            chain.doIntercept(channel, event);
                processor.afterProcessor(channel, event);
        }
    }

    }

