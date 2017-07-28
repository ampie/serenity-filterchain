package net.serenitybdd.cucumber.filterchain;

import net.thucydides.core.model.TestTag;

public enum TagProperty {
    NAME {
        @Override
        public boolean isExactMatch(TestTag tag, String value) {
            return tag.getName().equals(value);
        }

        @Override
        public boolean isNull(TestTag tag) {
            return tag.getName() == null;
        }
    }, TYPE {
        @Override
        public boolean isExactMatch(TestTag tag, String value) {
            return tag.getType().equals(value);
        }

        @Override
        public boolean isNull(TestTag tag) {
            return tag.getType() == null;
        }

    }, SHORT_NAME {
        @Override
        public boolean isExactMatch(TestTag tag, String value) {
            return tag.getShortName().equals(value);
        }

        @Override
        public boolean isNull(TestTag tag) {
            return tag.getShortName() == null;
        }
    }, COMPLETE_NAME {
        @Override
        public boolean isExactMatch(TestTag tag, String value) {
            return tag.getCompleteName().equals(value);
        }

        @Override
        public boolean isNull(TestTag tag) {
            return tag.getCompleteName() == null;
        }
    }, COMPLETE_NAME_CONTAINS {
        @Override
        public boolean isExactMatch(TestTag tag, String value) {
            return tag.getCompleteName().contains(value);
        }

        @Override
        public boolean isNull(TestTag tag) {
            return tag.getCompleteName() == null;
        }
    };

    protected boolean isExactMatch(TestTag tag, String value) {
        return false;
    }
    public boolean isMatch(TestTag tag, String value) {
        if(value.startsWith("@")){
            value=value.substring(1);
        }
        return isExactMatch(tag, value);
    }

    public boolean isNull(TestTag tag) {
        return false;
    }
}
