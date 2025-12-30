package Others.AmbientContext;

/**
 * User Context - Ambient Context Pattern Implementation
 *
 * Provides thread-local storage for the current user.
 * This allows any code in the call chain to access the current user
 * without explicitly passing it as a parameter.
 *
 * Key Features:
 * - ThreadLocal storage (each thread has its own user)
 * - Static access (available anywhere)
 * - Automatic cleanup support
 *
 * Usage:
 * <pre>
 * // Set current user (e.g., after login)
 * UserContext.setCurrent(user);
 *
 * // Access from anywhere in the call chain
 * User currentUser = UserContext.getCurrent();
 *
 * // Clear when done (important!)
 * UserContext.clear();
 * </pre>
 */
public class UserContext {

    // ThreadLocal storage - each thread has its own User
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    /**
     * Private constructor - this is a static utility class
     */
    private UserContext() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Set the current user for this thread
     *
     * @param user The user to set as current
     */
    public static void setCurrent(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null. Use clear() to remove user.");
        }

        currentUser.set(user);
        log("User context set: " + user.getUsername() + " (Thread: " +
            Thread.currentThread().getName() + ")");
    }

    /**
     * Get the current user for this thread
     *
     * @return The current user, or null if not set
     */
    public static User getCurrent() {
        User user = currentUser.get();

        if (user == null) {
            log("⚠️  Warning: No user in context (Thread: " +
                Thread.currentThread().getName() + ")");
        }

        return user;
    }

    /**
     * Get the current user, throw exception if not set
     *
     * @return The current user
     * @throws IllegalStateException if no user is set
     */
    public static User getCurrentRequired() {
        User user = currentUser.get();

        if (user == null) {
            throw new IllegalStateException(
                "No user in context. Did you forget to call UserContext.setCurrent()?"
            );
        }

        return user;
    }

    /**
     * Check if there is a current user set
     *
     * @return true if user is set, false otherwise
     */
    public static boolean hasCurrentUser() {
        return currentUser.get() != null;
    }

    /**
     * Clear the current user from this thread
     *
     * IMPORTANT: Always call this when done to prevent memory leaks!
     */
    public static void clear() {
        User user = currentUser.get();

        if (user != null) {
            log("User context cleared: " + user.getUsername() + " (Thread: " +
                Thread.currentThread().getName() + ")");
        }

        currentUser.remove();
    }

    /**
     * Execute code with a specific user context
     *
     * Automatically sets and clears the user context.
     *
     * @param user The user to execute as
     * @param action The action to execute
     */
    public static void executeAs(User user, Runnable action) {
        User previousUser = currentUser.get();

        try {
            setCurrent(user);
            action.run();
        } finally {
            if (previousUser != null) {
                currentUser.set(previousUser);
            } else {
                clear();
            }
        }
    }

    /**
     * Get the current user's ID
     *
     * @return User ID, or null if no user
     */
    public static Long getCurrentUserId() {
        User user = getCurrent();
        return user != null ? user.getId() : null;
    }

    /**
     * Get the current user's username
     *
     * @return Username, or "Anonymous" if no user
     */
    public static String getCurrentUsername() {
        User user = getCurrent();
        return user != null ? user.getUsername() : "Anonymous";
    }

    /**
     * Check if current user is admin
     *
     * @return true if current user is admin, false otherwise
     */
    public static boolean isCurrentUserAdmin() {
        User user = getCurrent();
        return user != null && user.isAdmin();
    }

    /**
     * Require admin role for current user
     *
     * @throws SecurityException if current user is not admin
     */
    public static void requireAdmin() {
        User user = getCurrentRequired();

        if (!user.isAdmin()) {
            throw new SecurityException(
                "Access denied. Admin role required. Current user: " + user.getUsername()
            );
        }
    }

    /**
     * Require specific role for current user
     *
     * @param role The required role
     * @throws SecurityException if current user doesn't have the role
     */
    public static void requireRole(String role) {
        User user = getCurrentRequired();

        if (!user.hasRole(role)) {
            throw new SecurityException(
                "Access denied. Role '" + role + "' required. Current user role: " + user.getRole()
            );
        }
    }

    /**
     * Log context operations
     */
    private static void log(String message) {
        System.out.println("[UserContext] " + message);
    }
}
