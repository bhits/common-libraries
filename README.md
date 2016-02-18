# MHC Common Libraries

---

## 1. Versioning

The versioning convention for common libraries:

 + Releases: `<MajorVersion>.<MinorVersion>.<IncrementalVersion>`
 + Snapshots: `<MajorVersion>.<MinorVersion>.<IncrementalVersion>-SNAPSHOT`

---

## 2. Manual Release Process

1. Pick the commit to cut the release
2. Create a temporary branch for the release from the selected commit
3. Checkout the temporary branch
4. Set the version to release (ie: if it was `1.0.3-SNAPSHOT`, move it to `1.0.3`)
5. Commit the release version on the temporary branch. The commit message should be something like `Release version 1.0.3`
6. Tag this final release commit with the version number (ie: `1.0.3`)
7. Delete the temporary branch
8. Push the tag
9. Checkout base branch (ie: master) and move the version to the next development version (ie: `1.0.4-SNAPSHOT`)

---

## 3. Branching

A new branch can be created right before a minor or major version upgrade, so the older versions can continue to have incremental versions for improvement and mostly hot fixes as necessary.

Example: Given the current development version `1.0.3-SNAPSHOT` and previous releases `1.0.0`, `1.0.1`, `1.0.2`, if it is decided to move the development to next minor version `1.1.0-SNAPSHOT`:
+ Create a branch named `1.0.x` from master
+ Move master to `1.1.0-SNAPSHOT` and commit
+ Push master and the new branch `1.0.x`

If there is any development required in an older version, all the new commits must be gradually merged to the branches newer than that version up until the master branch.

Example: If a hot fix is required in an older `1.2.x` branch with the final released version `1.2.4` and current development version `1.2.5-SNAPSHOT`:

 1. Fix the bug on `1.2.x` branch
 2. Release version `1.2.5` by following the manual release process
 3. Merge `1.2.x` to `1.3.x`, merge `1.3.x` to `1.4.x` ...etc
 5. Finally merge `1.4.x` to `master`

 **NOTE: It is highly recommended to use the    most recent releases of the most recent versions in the client projects.**