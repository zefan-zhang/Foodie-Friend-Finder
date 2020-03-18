# Foodie-Friend Finder

## Git Workflow

- We are using the GitFlow Process for branch management!

1. `git branch` -- Confirm you are on develop branch first, if not switch to it.
1. `git fetch` -- Grab latest metadata from develop to your local machine.
1. `git pull` -- Pull down the latest changes based on what you saw from fetch, from the remote develop branch.
1. `git checkout -b branch_name` -- Create your own feature branch that will be merged into develop once you are done.
1. Make some changes to files/folders.
1. `git add .` or `git add file/folders` -- Stage.
1. `git commit -m 'your message'`
1. `git checkout develop` -- Switch to `develop` for merging.
1. `git merge feature_branch_name` -- Merge your feature branch into develop.

## Additional Reading:

- [GitFlow Atlassian](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow)

