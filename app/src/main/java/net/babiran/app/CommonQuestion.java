package net.babiran.app;

import com.github.captain_miao.recyclerviewutils.stickyandexpandable.StickyHeaderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alireza on 1/22/2018.
 */

public class CommonQuestion implements StickyHeaderItem {
    public int id;
    public String question;
    public String answer;
    public String subject;
    public int order;
    public String created_at;
    public String updated_at;
    public ArrayList<CommonQuestion> childs = new ArrayList<>();
    public boolean isParent = false;
    public int hraderId;

    public CommonQuestion() {

    }

    public CommonQuestion clone() {
        CommonQuestion commonQuestion = new CommonQuestion();
        commonQuestion.id = id;
        commonQuestion.question = question;
        commonQuestion.answer = answer;
        commonQuestion.subject = subject;
        commonQuestion.order = order;
        commonQuestion.created_at = created_at;
        commonQuestion.updated_at = updated_at;
        commonQuestion.childs = childs;
        commonQuestion.isParent = isParent;
        commonQuestion.hraderId = hraderId;
        return commonQuestion;
    }

    @Override
    public long getHeaderId() {
        return this.hraderId;
    }

    @Override
    public boolean isParentItem() {
        return isParent;
    }

    @Override
    public boolean isExpanded() {
        return true;
    }

    @Override
    public void setExpanded(boolean b) {

    }

    @Override
    public List getChildItemList() {
        return childs;
    }
}
