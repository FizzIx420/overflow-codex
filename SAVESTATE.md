# Savestate


## 2026-10-24: Rolling release ZIP publisher and recovery policy

- Added `.github/actions/publish-release-zip/action.yml` for ZIP publication, rebuild metadata, SHA-256 inventory, and rolling three-release retention.
- Added `.github/RELEASE_ZIP_POLICY.md` describing the required publication pattern.
- Existing workflows are not yet migrated in this commit. Verification of actual publication/retention is pending a successful build.

## 2026-10-24: Migrate build output to rolling release ZIPs

- Updated `.github/workflows/build.yml` to publish the produced JAR as a ZIP GitHub Release asset using `.github/actions/publish-release-zip` instead of storing it as a long-lived Actions artifact.
- Added explicit `contents: write` workflow permission for release publication.
- Build commands and source packaging steps were left unchanged; publication runs only after a successful non-PR build and only when a JAR exists.
- The publisher embeds commit/run recovery metadata and file SHA-256 values, and retains the newest three successful CI releases.
- Verification pending a successful Actions run; this tool session did not dispatch the workflow.
