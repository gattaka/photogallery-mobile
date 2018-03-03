package cz.gattserver.mobile.common;

import com.codename1.ui.Graphics;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ScrollListener;

public class CustomToolbar extends Toolbar implements ScrollListener {
	private int alpha;

	public CustomToolbar() {
	}

	@Override
	public void paintComponentBackground(Graphics g) {
		int a = g.getAlpha();
		g.setAlpha(alpha);
		super.paintComponentBackground(g);
		g.setAlpha(a);
	}

	@Override
	public void scrollChanged(int scrollX, int scrollY, int oldscrollX, int oldscrollY) {
		alpha = scrollY;
		alpha = Math.max(alpha, 0);
		alpha = Math.min(alpha, 255);
	}
}