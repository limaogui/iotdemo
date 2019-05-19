/* 
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cloudworker.iot.charge;

import com.alibaba.fastjson.JSON;
import com.baidu.dueros.bot.BaseBot;
import com.baidu.dueros.data.request.IntentRequest;
import com.baidu.dueros.data.request.LaunchRequest;
import com.baidu.dueros.data.request.pay.event.ChargeEvent;
import com.baidu.dueros.data.response.OutputSpeech;
import com.baidu.dueros.data.response.OutputSpeech.SpeechType;
import com.baidu.dueros.data.response.Reprompt;
import com.baidu.dueros.data.response.card.TextCard;
import com.baidu.dueros.data.response.directive.pay.Charge;
import com.baidu.dueros.model.Response;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 返回支付相关的例子，继承{@code BaseBot}类
 * 
 * @author limaogui
 * @version V1.0
 */
public class ChargeBot extends BaseBot {

    protected ChargeBot(HttpServletRequest request) throws IOException {
        super(request);
    }

    protected Response onLaunch(LaunchRequest launchRequest) {
    	System.out.println("进入onLaunch方法");
        TextCard textCard = new TextCard("感谢您参与募捐服务");
        OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, "感谢您参与募捐服务");
        // 构造返回的Response
        Response response = new Response(outputSpeech, textCard);
        System.out.println("outputSpeech:"+JSON.toJSONString(outputSpeech));
        System.out.println("textCard:"+JSON.toJSONString(textCard));
        System.out.println("response:"+JSON.toJSONString(response));
        System.out.println("onLaunch方法结束");
        return response;
    }

    @Override
    protected Response onInent(IntentRequest intentRequest) {
    	System.out.println("进入onInent方法");
        // 判断NLU解析的意图名称是否匹配 charge
        if ("charge".equals(intentRequest.getIntentName())) {
        	System.out.println("进入onInent方法的if判断");
            // 构造返回的charge指令
            Charge charge = new Charge("0.01", "sellerOrderId", "寺庙募捐", "香客石庙募捐");
            charge.setSellerNote("sellerNote");
            charge.setSellerAuthorizationNote("sellerAuthorizationNote");
            charge.setToken("token");
            System.out.println("charge:"+JSON.toJSONString(charge));
            // 添加返回的charge指令
            this.addDirective(charge);

            OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, "感谢您的捐赠，请在屏幕上支付");
            // 构造返回的Response
            Response response = new Response(outputSpeech);
            System.out.println("onInent方法的if判断结束");
            return response;
        }
        System.out.println("onInent方法结束");
        return null;
    }

    @Override
    protected Response onChargeEvent(ChargeEvent chargeEvent) {
    	System.out.println("进入onChargeEvent方法");
    	System.out.println("chargeEvent------:"+JSON.toJSONString(chargeEvent));
        // 支付成功后，会收到ChargeEvent事件
        String purchaseResult = chargeEvent.getPurchaseResult();
        System.out.println("purchaseResult----:"+JSON.toJSONString(purchaseResult));
        // 可以从chargeEvent中获取到对应charge指令的token
        OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, "你已支付0.01元，谢谢，功德无量");
        TextCard textCard = new TextCard("你已支付0.01元，谢谢，功德无量");
        Reprompt reprompt = new Reprompt(outputSpeech);
        System.out.println("outputSpeech:"+JSON.toJSONString(outputSpeech));
        System.out.println("textCard:"+JSON.toJSONString(textCard));
        System.out.println("reprompt:"+JSON.toJSONString(reprompt));
        Response response = new Response(outputSpeech, textCard, reprompt);
        System.out.println("onChargeEvent方法结束");
        return response;
    }

}
