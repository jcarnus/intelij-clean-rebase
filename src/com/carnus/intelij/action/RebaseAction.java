package com.carnus.intelij.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.vcs.log.*;
import git4idea.GitCommit;
import git4idea.history.GitHistoryUtils;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryManager;

import java.util.List;
import java.util.Optional;

public class RebaseAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        final Project project = e.getProject();
        final VcsLog log = e.getRequiredData(VcsLogDataKeys.VCS_LOG);
        final VcsLogUi logUI = e.getRequiredData(VcsLogDataKeys.VCS_LOG_UI);
        final List<VcsRef> refs = e.getRequiredData(VcsLogDataKeys.VCS_LOG_BRANCHES);

        final List<CommitId> selectedCommits = log.getSelectedCommits();
        if (selectedCommits.size() != 1) {
            return;
        }

        final CommitId selectedCommit = selectedCommits.get(0);

        final GitRepositoryManager repositoryManager = GitRepositoryManager.getInstance(project);
        final GitRepository repository = repositoryManager.getRepositoryForRoot(selectedCommit.getRoot());
        final String currentBranchName = repository.getCurrentBranchName();
        final String selectedBranchName = refs.get(0).getName();

        try {
            final List<GitCommit> commitsOnCurrent = GitHistoryUtils.history(project, repository.getRoot(), currentBranchName);
            final List<GitCommit> commitsOnSelected = GitHistoryUtils.history(project, repository.getRoot(), selectedBranchName);
            final Optional<GitCommit> optionalGitCommit = commitsOnSelected.stream().filter(commitsOnCurrent::contains).findFirst();

            GitCommit firstCommitInCommon = optionalGitCommit.orElseThrow(() -> new RuntimeException(String.format("Current branch '%s' has not been forked from selected '%s'", currentBranchName, selectedBranchName)));

            System.out.println(firstCommitInCommon);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
