// Generated code from Butter Knife. Do not modify!
package com.cleanup.todoc.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.cleanup.todoc.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.listTasks = Utils.findRequiredViewAsType(source, R.id.list_tasks, "field 'listTasks'", RecyclerView.class);
    target.lblNoTasks = Utils.findRequiredViewAsType(source, R.id.lbl_no_task, "field 'lblNoTasks'", TextView.class);
    target.fab = Utils.findRequiredViewAsType(source, R.id.fab_add_task, "field 'fab'", FloatingActionButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.listTasks = null;
    target.lblNoTasks = null;
    target.fab = null;
  }
}
