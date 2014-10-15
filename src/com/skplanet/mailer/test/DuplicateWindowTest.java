package com.skplanet.mailer.test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.skplanet.cask.container.config.ConfigReader;
import com.skplanet.mailer.mail.DuplicatedWindow;
import com.skplanet.mailer.mail.Mailer;

public class DuplicateWindowTest {
	@Test
	public void testRemoveDuplicateParen() {
		try {
			Assert.assertEquals("1234", DuplicatedWindow.removeBracket("[my]1234[jjijef]"));
			Assert.assertEquals("1234567", DuplicatedWindow.removeBracket("[my]1234[jjijef]567"));
			Assert.assertEquals("01234567", DuplicatedWindow.removeBracket("0[my]1234[jjijef]567"));
			Assert.assertEquals("01234567", DuplicatedWindow.removeBracket("0[my]1234[jjijef]567[kkk]"));
			Assert.assertEquals("01234", DuplicatedWindow.removeBracket("0[my]1234"));
			Assert.assertEquals("01234", DuplicatedWindow.removeBracket("01234"));
			Assert.assertEquals("메일 테스트  내용  또 ", DuplicatedWindow.removeBracket("메일 테스트 [변경] 내용 [변경] 또 [변경]"));

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testDuplicate() {
		
		Date curr = new Date();
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		sendMail11();
		DuplicatedWindow.getInstance().setWindowTime("1");
		Mailer.getInstance().setTestMode(true);
			
		try {
			Mailer.getInstance().sendNotSent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testRemoveStaled() {
		
		sendMail11();
		DuplicatedWindow.getInstance().setWindowTime("1");
		Mailer.getInstance().setTestMode(true);
		
		try {
			// never send
			Mailer.getInstance().sendNotSent();
			Thread.sleep(1000 * 61);

			// send 2
			sendMail11();
			Mailer.getInstance().sendNotSent();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	private static void sendMail11() {
		int count = 0;
		Date curr = new Date();
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			for(int i = 0; i < 10; i++) {
				Mailer.getInstance().save(
						"test@daum.net",
						"testrec@daum.net",
						"My Test",
						"Content [" + sdf.format(curr) + "] Content test [" + i + "] ",
						"smtp.daum.net",
						"465",
						null,
						null,
						"false");
			}
			Mailer.getInstance().save(
					"test@daum.net",
					"testrec@daum.net",
					"My Test",
					"Content [" + sdf.format(curr) + "] Content test 11 [" + 11 + "] " ,
					"smtp.daum.net",
					"465",
					null,
					null,
					"false");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
