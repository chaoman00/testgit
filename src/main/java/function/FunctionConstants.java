package function;

public interface FunctionConstants {

	// 转人工队列功能常量
	static interface AcsSeq {
		public static final String ACS_SEQ_VALUE = "acsSeqValue";

		public static final String DEV_ACS_SEQ_SWITCH = "dev.acsSeqSwitch"; // 是否开启此功能
		public static final String DEV_ACS_SEQ_CONTENT_HEADER = "dev.acsSeqContentHeader"; // 队列列表头部内容
		public static final String DEV_ACS_SEQ_CONTENT = "dev.acsSeqContent"; // 队列列表内容
	}

	// 默认回复次数转人工功能常量
	static interface DefaultReplay {
		public static final String DEV_DEFAULT_REPLAY_SWITCH = "dev.defaultReplaySwitch"; // 是否开启此功能
	}

	// 解决、未解决功能常量
	static interface FaqVote {
		public static final String DEV_FAQ_VOTE_SWITCH = "dev.faqVoteSwitch"; // 是否开启此功能
	}

	// 序号递增功能常量
	static interface SeqNoIncrease {

		public static final String INCREASE_FLAG = "increaseFlag";

		public static final String DEV_SEQ_NO_INCREASE_SWITCH = "dev.seqNoIncreaseSwitch"; // 是否开启此功能
		public static final String DEV_SEQ_NO_SIZE = "dev.seqNoSize"; // 每次允许递增的个数
		public static final String DEV_SEQ_NO_LIMIT = "dev.seqNoLimit"; // 序号递增上限
		public static final String DEV_SEQ_NO_START = "dev.seqNoStart"; // 序号递增起始数字
		public static final String DEV_SEQ_NO_RENDER_STYLE = "dev.seqNoRenderStyle"; // 渲染后序号的样式

	}

}
