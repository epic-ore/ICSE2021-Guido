package guido.verification.systems.key.options.taclets;

import guido.core.verifier.OptionableContainer;


public enum MoreSeqRulesTaclet implements TacletOptionable{

	ON, OFF;
	
	@Override
	public String getValue() {
		return getType()+":"+name().toLowerCase();
	}

	@Override
	public OptionableContainer getOptionableContainer() {
		return KeyTacletOptions.MORE_SEQ_RULES;
	}

}
