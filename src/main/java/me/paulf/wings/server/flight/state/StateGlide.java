package me.paulf.wings.server.flight.state;

import me.paulf.wings.client.flight.Animator;

public final class StateGlide extends State {
	private static final int LIFT_STATE_DELAY = 6;

	public StateGlide() {
		super(Animator::beginGlide);
	}

	@Override
	protected State getLift() {
		return new StateLift(LIFT_STATE_DELAY);
	}

	@Override
	protected State getGlide() {
		return this;
	}
}
