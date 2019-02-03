package cz.gattserver.mobile.sms;

import com.codename1.components.SpanLabel;
import com.codename1.sms.intercept.SMSInterceptor;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.util.SuccessCallback;

import cz.gattserver.mobile.common.SwitchableForm;

public class SMSScreen extends SwitchableForm {

	public SMSScreen(SwitchableForm prevForm) {
		super("SMS", prevForm);
	}

	private void tryGrabSMS() {
		SMSInterceptor.grabNextSMS(new SuccessCallback<String>() {

			@Override
			public void onSucess(String value) {
				SpanLabel sl = new SpanLabel("SMS: " + value);
				add(sl);
				SMSScreen.this.tryGrabSMS();
			}
		});
	}

	@Override
	public SwitchableForm init() {
		setLayout(BoxLayout.y());
		setScrollableY(true);

		if (!SMSInterceptor.isSupported()) {
			SpanLabel sl = new SpanLabel("SMSInterceptor is not supported :(");
			add(sl);
		} else {
			tryGrabSMS();
		}

		revalidate();

		return this;
	}

}
