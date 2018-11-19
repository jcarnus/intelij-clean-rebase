package com.carnus.intelij.action;

import java.util.List;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.vcs.log.CommitId;
import com.intellij.vcs.log.VcsLog;
import com.intellij.vcs.log.VcsLogDataKeys;
import com.intellij.vcs.log.VcsLogUi;
import com.intellij.vcs.log.VcsRef;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryManager;

public class RebaseAction extends AnAction {

	@Override
	public void actionPerformed(AnActionEvent e) {

		Project project = e.getProject();

		VcsLog log = e.getData( VcsLogDataKeys.VCS_LOG );
		VcsLogUi logUI = e.getData( VcsLogDataKeys.VCS_LOG_UI );
		List<VcsRef> refs = e.getData( VcsLogDataKeys.VCS_LOG_BRANCHES );

		List<CommitId> commits = log.getSelectedCommits();
		if ( commits.size() != 1 ) {
			return;
		}

		CommitId commit = commits.get( 0 );

		GitRepositoryManager repositoryManager = GitRepositoryManager.getInstance( project );
		final GitRepository root = repositoryManager.getRepositoryForRoot( commit.getRoot() );

	}
}
