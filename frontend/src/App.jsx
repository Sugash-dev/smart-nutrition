import { useEffect, useMemo, useState } from "react";
import axios from "axios";
import {
  Apple,
  BarChart3,
  Check,
  ChevronRight,
  CircleUserRound,
  Flame,
  Lightbulb,
  LoaderCircle,
  Pencil,
  Plus,
  Salad,
  Trash2,
  Utensils,
  Users,
  X,
  Dumbbell,
  Wheat,
  Leaf,
} from "lucide-react";

import "./App.css";

const API = "http://localhost:8080/api";

/* =========================================================
   HELPERS
   ========================================================= */

const today = () => {
  const date = new Date();

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
};

const formatNumber = (value, digits = 1) => {
  const number = Number(value ?? 0);

  if (Number.isInteger(number)) {
    return number.toString();
  }

  return number.toFixed(digits);
};

const foodEmoji = (name = "") => {
  const value = name.toLowerCase();

  if (
      value.includes("rice") ||
      value.includes("biryani") ||
      value.includes("dal") ||
      value.includes("sambar") ||
      value.includes("chickpea")
  ) {
    return "🍚";
  }

  if (
      value.includes("chicken") ||
      value.includes("fish") ||
      value.includes("egg") ||
      value.includes("omelette")
  ) {
    return "🍗";
  }

  if (
      value.includes("paneer") ||
      value.includes("curd") ||
      value.includes("milk")
  ) {
    return "🥛";
  }

  if (
      value.includes("banana") ||
      value.includes("apple") ||
      value.includes("fruit")
  ) {
    return "🍌";
  }

  if (
      value.includes("idli") ||
      value.includes("dosa") ||
      value.includes("chapati")
  ) {
    return "🥘";
  }

  if (
      value.includes("vegetable") ||
      value.includes("salad")
  ) {
    return "🥗";
  }

  return "🍲";
};

/* =========================================================
   MAIN APP
   ========================================================= */

function App() {
  const [page, setPage] = useState("dashboard");

  const [users, setUsers] = useState([]);
  const [foods, setFoods] = useState([]);

  const [selectedUserId, setSelectedUserId] = useState("");

  const [currentUser, setCurrentUser] = useState(null);
  const [meals, setMeals] = useState([]);
  const [nutrition, setNutrition] = useState(null);
  const [recommendations, setRecommendations] = useState([]);

  const [loading, setLoading] = useState(true);
  const [pageLoading, setPageLoading] = useState(false);

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [showUserModal, setShowUserModal] = useState(false);
  const [showFoodModal, setShowFoodModal] = useState(false);
  const [showMealModal, setShowMealModal] = useState(false);

  const [editingUser, setEditingUser] = useState(null);
  const [editingFood, setEditingFood] = useState(null);

  /* =======================================================
     FORM STATES
     ======================================================= */

  const emptyUser = {
    name: "",
    email: "",
    age: "",
    gender: "MALE",
    heightCm: "",
    weightKg: "",
    activityLevel: "MODERATE",
    goal: "MAINTAIN",
    dietType: "VEG",
  };

  const emptyFood = {
    name: "",
    calories: "",
    proteinG: "",
    carbsG: "",
    fatG: "",
    dietType: "VEG",
  };

  const emptyMeal = {
    foodId: "",
    servings: 1,
  };

  const [userForm, setUserForm] = useState(emptyUser);
  const [foodForm, setFoodForm] = useState(emptyFood);
  const [mealForm, setMealForm] = useState(emptyMeal);

  /* =======================================================
     INITIAL LOAD
     ======================================================= */

  useEffect(() => {
    loadInitialData();
  }, []);

  useEffect(() => {
    if (selectedUserId) {
      loadUserData(selectedUserId);
    }
  }, [selectedUserId]);

  /* =======================================================
     LOAD USERS + FOODS
     ======================================================= */

  const loadInitialData = async () => {
    try {
      setLoading(true);
      setError("");

      const [usersResponse, foodsResponse] = await Promise.all([
        axios.get(`${API}/users`),
        axios.get(`${API}/foods`),
      ]);

      const loadedUsers = usersResponse.data || [];
      const loadedFoods = foodsResponse.data || [];

      setUsers(loadedUsers);
      setFoods(loadedFoods);

      if (loadedUsers.length > 0) {
        setSelectedUserId(String(loadedUsers[0].id));
      }
    } catch (err) {
      console.error(err);

      setError(
          getErrorMessage(err, "Unable to load application data.")
      );
    } finally {
      setLoading(false);
    }
  };

  /* =======================================================
     LOAD SELECTED USER DATA
     ======================================================= */

  const loadUserData = async (userId) => {
    try {
      setPageLoading(true);
      setError("");

      const date = today();

      const [
        userResponse,
        mealsResponse,
        nutritionResponse,
        recommendationResponse,
      ] = await Promise.all([
        axios.get(`${API}/users/${userId}`),
        axios.get(`${API}/meal-logs`, {
          params: {
            userId,
            date,
          },
        }),
        axios.get(`${API}/users/${userId}/nutrition-summary`, {
          params: {
            date,
          },
        }),
        axios.get(`${API}/users/${userId}/recommendations`),
      ]);

      setCurrentUser(userResponse.data);
      setMeals(mealsResponse.data || []);
      setNutrition(nutritionResponse.data || null);
      setRecommendations(recommendationResponse.data || []);
    } catch (err) {
      console.error(err);

      setError(
          getErrorMessage(err, "Unable to load user information.")
      );
    } finally {
      setPageLoading(false);
    }
  };

  /* =======================================================
     ERROR MESSAGE
     ======================================================= */

  const getErrorMessage = (err, fallback) => {
    if (err?.response?.data?.message) {
      return err.response.data.message;
    }

    if (typeof err?.response?.data === "string") {
      return err.response.data;
    }

    if (err?.message) {
      return err.message;
    }

    return fallback;
  };

  /* =======================================================
     REFRESH CURRENT USER
     ======================================================= */

  const refreshCurrentUser = async () => {
    if (!selectedUserId) return;

    await loadUserData(selectedUserId);
  };

  /* =======================================================
     USER CRUD
     ======================================================= */

  const openAddUser = () => {
    setEditingUser(null);
    setUserForm(emptyUser);
    setShowUserModal(true);
    clearMessages();
  };

  const openEditUser = (user) => {
    setEditingUser(user);

    setUserForm({
      name: user.name || "",
      email: user.email || "",
      age: user.age ?? "",
      gender: user.gender || "MALE",
      heightCm: user.heightCm ?? "",
      weightKg: user.weightKg ?? "",
      activityLevel: user.activityLevel || "MODERATE",
      goal: user.goal || "MAINTAIN",
      dietType: user.dietType || "VEG",
    });

    setShowUserModal(true);
    clearMessages();
  };

  const saveUser = async (event) => {
    event.preventDefault();

    try {
      setPageLoading(true);
      setError("");

      const payload = {
        name: userForm.name.trim(),
        email: userForm.email.trim(),
        age: Number(userForm.age),
        gender: userForm.gender,
        heightCm: Number(userForm.heightCm),
        weightKg: Number(userForm.weightKg),
        activityLevel: userForm.activityLevel,
        goal: userForm.goal,
        dietType: userForm.dietType,
      };

      let response;

      if (editingUser) {
        response = await axios.put(
            `${API}/users/${editingUser.id}`,
            payload
        );

        setSuccess("User updated successfully.");
      } else {
        response = await axios.post(`${API}/users`, payload);

        setSuccess("User created successfully.");
      }

      setShowUserModal(false);

      const usersResponse = await axios.get(`${API}/users`);
      const updatedUsers = usersResponse.data || [];

      setUsers(updatedUsers);

      if (!editingUser && response?.data?.id) {
        setSelectedUserId(String(response.data.id));
      } else if (editingUser) {
        setSelectedUserId(String(editingUser.id));
      }
    } catch (err) {
      console.error(err);

      setError(
          getErrorMessage(err, "Unable to save user.")
      );
    } finally {
      setPageLoading(false);
    }
  };

  const deleteUser = async (userId) => {
    const confirmed = window.confirm(
        "Are you sure you want to delete this user?"
    );

    if (!confirmed) return;

    try {
      setPageLoading(true);
      setError("");

      await axios.delete(`${API}/users/${userId}`);

      const remainingUsers = users.filter(
          (user) => user.id !== userId
      );

      setUsers(remainingUsers);

      if (String(selectedUserId) === String(userId)) {
        if (remainingUsers.length > 0) {
          setSelectedUserId(String(remainingUsers[0].id));
        } else {
          setSelectedUserId("");
          setCurrentUser(null);
          setMeals([]);
          setNutrition(null);
          setRecommendations([]);
        }
      }

      setSuccess("User deleted successfully.");
    } catch (err) {
      console.error(err);

      setError(
          getErrorMessage(err, "Unable to delete user.")
      );
    } finally {
      setPageLoading(false);
    }
  };

  /* =======================================================
     FOOD CRUD
     ======================================================= */

  const openAddFood = () => {
    setEditingFood(null);
    setFoodForm(emptyFood);
    setShowFoodModal(true);
    clearMessages();
  };

  const openEditFood = (food) => {
    setEditingFood(food);

    setFoodForm({
      name: food.name || "",
      calories: food.calories ?? "",
      proteinG: food.proteinG ?? "",
      carbsG: food.carbsG ?? "",
      fatG: food.fatG ?? "",
      dietType: food.dietType || "VEG",
    });

    setShowFoodModal(true);
    clearMessages();
  };

  const saveFood = async (event) => {
    event.preventDefault();

    try {
      setPageLoading(true);
      setError("");

      const payload = {
        name: foodForm.name.trim(),
        calories: Number(foodForm.calories),
        proteinG: Number(foodForm.proteinG),
        carbsG: Number(foodForm.carbsG),
        fatG: Number(foodForm.fatG),
        dietType: foodForm.dietType,
      };

      if (editingFood) {
        await axios.put(
            `${API}/foods/${editingFood.id}`,
            payload
        );

        setSuccess("Food updated successfully.");
      } else {
        await axios.post(`${API}/foods`, payload);

        setSuccess("Food created successfully.");
      }

      setShowFoodModal(false);

      const foodsResponse = await axios.get(`${API}/foods`);

      setFoods(foodsResponse.data || []);

      await refreshCurrentUser();
    } catch (err) {
      console.error(err);

      setError(
          getErrorMessage(err, "Unable to save food.")
      );
    } finally {
      setPageLoading(false);
    }
  };

  const deleteFood = async (foodId) => {
    const confirmed = window.confirm(
        "Are you sure you want to delete this food?"
    );

    if (!confirmed) return;

    try {
      setPageLoading(true);
      setError("");

      await axios.delete(`${API}/foods/${foodId}`);

      setFoods((previous) =>
          previous.filter((food) => food.id !== foodId)
      );

      setSuccess("Food deleted successfully.");
    } catch (err) {
      console.error(err);

      setError(
          getErrorMessage(err, "Unable to delete food.")
      );
    } finally {
      setPageLoading(false);
    }
  };

  /* =======================================================
     MEAL CRUD
     ======================================================= */

  const openAddMeal = (food = null) => {
    setMealForm({
      foodId: food ? String(food.id) : "",
      servings: 1,
    });

    setShowMealModal(true);
    clearMessages();
  };

  const addMeal = async (event) => {
    event.preventDefault();

    if (!selectedUserId) {
      setError("Please select a user first.");
      return;
    }

    if (!mealForm.foodId) {
      setError("Please select a food.");
      return;
    }

    try {
      setPageLoading(true);
      setError("");

      const payload = {
        userId: Number(selectedUserId),
        foodId: Number(mealForm.foodId),
        logDate: today(),
        servings: Number(mealForm.servings),
      };

      await axios.post(`${API}/meal-logs`, payload);

      setShowMealModal(false);

      setSuccess("Meal added successfully.");

      await refreshCurrentUser();
    } catch (err) {
      console.error(err);

      setError(
          getErrorMessage(err, "Unable to add meal.")
      );
    } finally {
      setPageLoading(false);
    }
  };

  const deleteMeal = async (mealId) => {
    const confirmed = window.confirm(
        "Remove this meal from today's log?"
    );

    if (!confirmed) return;

    try {
      setPageLoading(true);
      setError("");

      await axios.delete(`${API}/meal-logs/${mealId}`);

      setSuccess("Meal removed successfully.");

      await refreshCurrentUser();
    } catch (err) {
      console.error(err);

      setError(
          getErrorMessage(err, "Unable to delete meal.")
      );
    } finally {
      setPageLoading(false);
    }
  };

  /* =======================================================
     ALERTS
     ======================================================= */

  const clearMessages = () => {
    setError("");
    setSuccess("");
  };

  /* =======================================================
     NAVIGATION
     ======================================================= */

  const navigate = (target) => {
    setPage(target);
    clearMessages();
  };

  /* =======================================================
     NUTRITION VALUES
     ======================================================= */

  const consumedCalories = Number(
      nutrition?.consumedCalories ?? 0
  );

  const consumedProtein = Number(
      nutrition?.consumedProtein ?? 0
  );

  const consumedCarbs = Number(
      nutrition?.consumedCarbs ?? 0
  );

  const consumedFat = Number(
      nutrition?.consumedFat ?? 0
  );

  const targetCalories = Number(
      nutrition?.targetCalories ??
      currentUser?.dailyCalorieTarget ??
      0
  );

  const remainingCalories = Math.max(
      targetCalories - consumedCalories,
      0
  );

  /* =======================================================
     FILTER RECOMMENDATIONS
     ======================================================= */

  const visibleRecommendations = useMemo(() => {
    return Array.isArray(recommendations)
        ? recommendations.slice(0, 6)
        : [];
  }, [recommendations]);

  /* =======================================================
     LOADING SCREEN
     ======================================================= */

  if (loading) {
    return (
        <div className="loading-screen">
          <LoaderCircle
              size={40}
              className="spin"
          />

          <p>Loading Smart Nutrition...</p>
        </div>
    );
  }

  /* =======================================================
     MAIN UI
     ======================================================= */

  return (
      <div className="app">

        {/* ===================================================
          SIDEBAR
          =================================================== */}

        <aside className="sidebar">

          <div className="brand">

            <div className="brand-icon">
              🍲
            </div>

            <div>
              <h2>Smart Nutrition</h2>
              <span>Health & Wellness</span>
            </div>

          </div>

          <nav>

            <button
                className={`nav-item ${
                    page === "dashboard" ? "active" : ""
                }`}
                onClick={() => navigate("dashboard")}
            >
              <BarChart3 size={18} />
              <span>Dashboard</span>
            </button>

            <button
                className={`nav-item ${
                    page === "users" ? "active" : ""
                }`}
                onClick={() => navigate("users")}
            >
              <Users size={18} />
              <span>Users</span>
            </button>

            <button
                className={`nav-item ${
                    page === "foods" ? "active" : ""
                }`}
                onClick={() => navigate("foods")}
            >
              <Apple size={18} />
              <span>Foods</span>
            </button>

            <button
                className={`nav-item ${
                    page === "meals" ? "active" : ""
                }`}
                onClick={() => navigate("meals")}
            >
              <Utensils size={18} />
              <span>My Meals</span>
            </button>

            <button
                className={`nav-item ${
                    page === "recommendations"
                        ? "active"
                        : ""
                }`}
                onClick={() => navigate("recommendations")}
            >
              <Lightbulb size={18} />
              <span>Recommendations</span>
            </button>

          </nav>

          <div className="sidebar-tip">

            <div className="tip-icon">
              💡
            </div>

            <div>
              <strong>Nutrition Tip</strong>

              <p>
                Stay hydrated and include
                vegetables in your meals.
              </p>
            </div>

          </div>

        </aside>

        {/* ===================================================
          MAIN
          =================================================== */}

        <main className="main">

          {/* =================================================
            TOP BAR
            ================================================= */}

          <header className="topbar">

            <div className="mobile-title">
              <strong>Smart Nutrition</strong>
            </div>

            <div className="user-selector">

              <span>Current User</span>

              <select
                  value={selectedUserId}
                  onChange={(event) =>
                      setSelectedUserId(event.target.value)
                  }
              >

                {users.length === 0 ? (
                    <option value="">
                      No users
                    </option>
                ) : (
                    users.map((user) => (
                        <option
                            key={user.id}
                            value={user.id}
                        >
                          {user.name}
                        </option>
                    ))
                )}

              </select>

            </div>

            <div className="top-user">

              <div className="top-avatar">
                {currentUser?.name
                    ?.charAt(0)
                    ?.toUpperCase() || "U"}
              </div>

              <div>
                <strong>
                  {currentUser?.name || "User"}
                </strong>

                <span>
                Healthy Living
              </span>
              </div>

            </div>

          </header>

          {/* =================================================
            CONTENT
            ================================================= */}

          <section className="content">

            {/* ALERTS */}

            {error && (
                <div className="alert error">

                  <X size={18} />

                  <span>{error}</span>

                  <button
                      onClick={() => setError("")}
                  >
                    <X size={15} />
                  </button>

                </div>
            )}

            {success && (
                <div className="alert success">

                  <Check size={18} />

                  <span>{success}</span>

                  <button
                      onClick={() => setSuccess("")}
                  >
                    <X size={15} />
                  </button>

                </div>
            )}

            {/* PAGE LOADING */}

            {pageLoading && (
                <div
                    style={{
                      position: "fixed",
                      top: 88,
                      right: 25,
                      zIndex: 500,
                      background: "white",
                      borderRadius: 10,
                      padding: "9px 13px",
                      boxShadow:
                          "0 5px 20px rgba(0,0,0,0.12)",
                      display: "flex",
                      alignItems: "center",
                      gap: 8,
                      color: "#16865e",
                      fontSize: 12,
                    }}
                >
                  <LoaderCircle
                      size={15}
                      className="spin"
                  />
                  Updating...
                </div>
            )}

            {/* =================================================
              DASHBOARD
              ================================================= */}

            {page === "dashboard" && (
                <>
                  <div className="hero">

                    <div>

                      <div className="hero-small">
                        DAILY NUTRITION
                      </div>

                      <h1>
                        Welcome back,{" "}
                        {currentUser?.name || "User"}! 👋
                      </h1>

                      <p>
                        Here's your personalized
                        nutrition overview for today.
                      </p>

                    </div>

                    <div className="daily-target">

                      <span>Daily Target</span>

                      <strong>
                        {Math.round(targetCalories)}
                      </strong>

                      <small>
                        kcal / day
                      </small>

                    </div>

                  </div>

                  {/* STATS */}

                  <div className="stats-grid">

                    <StatCard
                        icon={<Flame size={24} />}
                        iconClass="orange"
                        label="Calories"
                        value={`${formatNumber(
                            consumedCalories
                        )} / ${Math.round(
                            targetCalories
                        )}`}
                        suffix="kcal"
                    />

                    <StatCard
                        icon={<Dumbbell size={24} />}
                        iconClass="blue"
                        label="Protein"
                        value={formatNumber(
                            consumedProtein
                        )}
                        suffix="g today's intake"
                    />

                    <StatCard
                        icon={<Wheat size={24} />}
                        iconClass="yellow"
                        label="Carbohydrates"
                        value={formatNumber(
                            consumedCarbs
                        )}
                        suffix="g today's intake"
                    />

                    <StatCard
                        icon={<Leaf size={24} />}
                        iconClass="green"
                        label="Fat"
                        value={formatNumber(
                            consumedFat
                        )}
                        suffix="g today's intake"
                    />

                  </div>

                  {/* DASHBOARD GRID */}

                  <div className="dashboard-grid">

                    {/* TODAY'S MEALS */}

                    <section className="panel">

                      <div className="panel-header">

                        <div>
                          <h2>Today's Meals</h2>

                          <p>
                            Your meals logged today
                          </p>
                        </div>

                        <button
                            className="primary-btn"
                            onClick={() =>
                                openAddMeal()
                            }
                        >
                          <Plus size={15} />
                          Add Meal
                        </button>

                      </div>

                      {meals.length === 0 ? (
                          <EmptyState
                              icon={
                                <Utensils size={30} />
                              }
                              message="No meals logged today."
                          />
                      ) : (
                          <div className="meal-list">

                            {meals.map((meal) => (

                                <div
                                    className="meal-row"
                                    key={meal.id}
                                >

                                  <div className="meal-icon">
                                    {foodEmoji(
                                        meal.food?.name
                                    )}
                                  </div>

                                  <div className="meal-info">

                                    <strong>
                                      {meal.food?.name ||
                                          "Food"}
                                    </strong>

                                    <span>
                              {meal.servings}{" "}
                                      {Number(
                                          meal.servings
                                      ) === 1
                                          ? "serving"
                                          : "servings"}
                            </span>

                                  </div>

                                  <button
                                      className="delete-btn"
                                      onClick={() =>
                                          deleteMeal(
                                              meal.id
                                          )
                                      }
                                  >
                                    <Trash2
                                        size={13}
                                    />
                                    Delete
                                  </button>

                                </div>

                            ))}

                          </div>
                      )}

                    </section>

                    {/* RECOMMENDATIONS */}

                    <section className="panel">

                      <div className="panel-header">

                        <div>
                          <h2>
                            Recommended Foods
                          </h2>

                          <p>
                            Personalized for your goal
                          </p>
                        </div>

                        <button
                            className="text-btn"
                            onClick={() =>
                                navigate(
                                    "recommendations"
                                )
                            }
                        >
                          View All
                          <ChevronRight
                              size={14}
                              style={{
                                verticalAlign: "middle",
                              }}
                          />
                        </button>

                      </div>

                      {visibleRecommendations.length ===
                      0 ? (
                          <EmptyState
                              icon={
                                <Lightbulb size={30} />
                              }
                              message="No recommendations available."
                          />
                      ) : (
                          <div className="recommendation-list">

                            {visibleRecommendations
                                .slice(0, 5)
                                .map((food) => (

                                    <div
                                        className="recommendation-row"
                                        key={food.id}
                                    >

                                      <div className="food-icon">
                                        {foodEmoji(
                                            food.name
                                        )}
                                      </div>

                                      <div
                                          style={{
                                            flex: 1,
                                          }}
                                      >
                                        <strong>
                                          {food.name}
                                        </strong>

                                        <span>
                                {formatNumber(
                                    food.calories,
                                    0
                                )}{" "}
                                          kcal
                              </span>
                                      </div>

                                    </div>

                                ))}

                          </div>
                      )}

                    </section>

                  </div>

                </>
            )}

            {/* =================================================
              USERS
              ================================================= */}

            {page === "users" && (
                <>

                  <div className="page-title">

                    <div>
                      <div className="page-label">
                        SMART NUTRITION
                      </div>

                      <h1>Users</h1>
                    </div>

                    <button
                        className="primary-btn"
                        onClick={openAddUser}
                    >
                      <Plus size={16} />
                      Add User
                    </button>

                  </div>

                  <section className="panel">

                    <div className="panel-header">

                      <div>
                        <h2>
                          User Management
                        </h2>

                        <p>
                          Create and manage
                          nutrition profiles
                        </p>
                      </div>

                    </div>

                    {users.length === 0 ? (
                        <EmptyState
                            icon={
                              <Users size={35} />
                            }
                            message="No users found."
                        />
                    ) : (
                        <div className="table-wrapper">

                          <table>

                            <thead>

                            <tr>
                              <th>NAME</th>
                              <th>EMAIL</th>
                              <th>AGE</th>
                              <th>GOAL</th>
                              <th>DIET</th>
                              <th>CALORIES</th>
                              <th>ACTIONS</th>
                            </tr>

                            </thead>

                            <tbody>

                            {users.map((user) => (

                                <tr key={user.id}>

                                  <td>
                                    <strong>
                                      {user.name}
                                    </strong>
                                  </td>

                                  <td>
                                    {user.email}
                                  </td>

                                  <td>
                                    {user.age}
                                  </td>

                                  <td>
                                    {user.goal}
                                  </td>

                                  <td>
                                    {user.dietType}
                                  </td>

                                  <td>
                                    {Math.round(
                                        user.dailyCalorieTarget ||
                                        0
                                    )}{" "}
                                    kcal
                                  </td>

                                  <td>

                                    <div className="action-buttons">

                                      <button
                                          className="icon-btn"
                                          title="Edit"
                                          onClick={() =>
                                              openEditUser(
                                                  user
                                              )
                                          }
                                      >
                                        <Pencil
                                            size={15}
                                        />
                                      </button>

                                      <button
                                          className="icon-btn danger"
                                          title="Delete"
                                          onClick={() =>
                                              deleteUser(
                                                  user.id
                                              )
                                          }
                                      >
                                        <Trash2
                                            size={15}
                                        />
                                      </button>

                                    </div>

                                  </td>

                                </tr>

                            ))}

                            </tbody>

                          </table>

                        </div>
                    )}

                  </section>

                </>
            )}

            {/* =================================================
              FOODS
              ================================================= */}

            {page === "foods" && (
                <>

                  <div className="page-title">

                    <div>
                      <div className="page-label">
                        FOOD DATABASE
                      </div>

                      <h1>Foods</h1>
                    </div>

                    <button
                        className="primary-btn"
                        onClick={openAddFood}
                    >
                      <Plus size={16} />
                      Add Food
                    </button>

                  </div>

                  <section className="panel">

                    <div className="panel-header">

                      <div>
                        <h2>
                          Food Management
                        </h2>

                        <p>
                          Manage nutritional
                          information
                        </p>
                      </div>

                    </div>

                    {foods.length === 0 ? (
                        <EmptyState
                            icon={
                              <Apple size={35} />
                            }
                            message="No foods found."
                        />
                    ) : (
                        <div className="table-wrapper">

                          <table>

                            <thead>

                            <tr>
                              <th>FOOD</th>
                              <th>CALORIES</th>
                              <th>PROTEIN</th>
                              <th>CARBS</th>
                              <th>FAT</th>
                              <th>DIET</th>
                              <th>ACTIONS</th>
                            </tr>

                            </thead>

                            <tbody>

                            {foods.map((food) => (

                                <tr key={food.id}>

                                  <td>
                                    <strong>
                                      {foodEmoji(
                                          food.name
                                      )}{" "}
                                      {food.name}
                                    </strong>
                                  </td>

                                  <td>
                                    {food.calories} kcal
                                  </td>

                                  <td>
                                    {food.proteinG} g
                                  </td>

                                  <td>
                                    {food.carbsG} g
                                  </td>

                                  <td>
                                    {food.fatG} g
                                  </td>

                                  <td>
                                    {food.dietType}
                                  </td>

                                  <td>

                                    <div className="action-buttons">

                                      <button
                                          className="icon-btn"
                                          title="Edit"
                                          onClick={() =>
                                              openEditFood(
                                                  food
                                              )
                                          }
                                      >
                                        <Pencil
                                            size={15}
                                        />
                                      </button>

                                      <button
                                          className="icon-btn danger"
                                          title="Delete"
                                          onClick={() =>
                                              deleteFood(
                                                  food.id
                                              )
                                          }
                                      >
                                        <Trash2
                                            size={15}
                                        />
                                      </button>

                                    </div>

                                  </td>

                                </tr>

                            ))}

                            </tbody>

                          </table>

                        </div>
                    )}

                  </section>

                </>
            )}

            {/* =================================================
              MEALS
              ================================================= */}

            {page === "meals" && (
                <>

                  <div className="page-title">

                    <div>
                      <div className="page-label">
                        DAILY TRACKING
                      </div>

                      <h1>My Meals</h1>
                    </div>

                    <button
                        className="primary-btn"
                        onClick={() =>
                            openAddMeal()
                        }
                    >
                      <Plus size={16} />
                      Add Meal
                    </button>

                  </div>

                  <section className="panel">

                    <div className="panel-header">

                      <div>
                        <h2>
                          Today's Meals
                        </h2>

                        <p>
                          {currentUser?.name ||
                              "User"}
                          's nutrition log
                        </p>
                      </div>

                    </div>

                    {meals.length === 0 ? (
                        <EmptyState
                            icon={
                              <Utensils size={35} />
                            }
                            message="No meals logged today."
                        />
                    ) : (
                        <div className="meal-list large">

                          {meals.map((meal) => {

                            const food =
                                meal.food || {};

                            return (
                                <div
                                    className="meal-card"
                                    key={meal.id}
                                >

                                  <div className="food-icon large-icon">
                                    {foodEmoji(
                                        food.name
                                    )}
                                  </div>

                                  <div
                                      style={{
                                        flex: 1,
                                      }}
                                  >

                                    <h3>
                                      {food.name ||
                                          "Food"}
                                    </h3>

                                    <p>
                                      {meal.servings}{" "}
                                      {Number(
                                          meal.servings
                                      ) === 1
                                          ? "serving"
                                          : "servings"}
                                    </p>

                                    <div className="meal-nutrition">

                              <span>
                                {formatNumber(
                                    Number(
                                        food.calories ||
                                        0
                                    ) *
                                    Number(
                                        meal.servings ||
                                        0
                                    ),
                                    0
                                )}{" "}
                                kcal
                              </span>

                                      <span>
                                P:{" "}
                                        {formatNumber(
                                            Number(
                                                food.proteinG ||
                                                0
                                            ) *
                                            Number(
                                                meal.servings ||
                                                0
                                            )
                                        )}
                                        g
                              </span>

                                      <span>
                                C:{" "}
                                        {formatNumber(
                                            Number(
                                                food.carbsG ||
                                                0
                                            ) *
                                            Number(
                                                meal.servings ||
                                                0
                                            )
                                        )}
                                        g
                              </span>

                                      <span>
                                F:{" "}
                                        {formatNumber(
                                            Number(
                                                food.fatG ||
                                                0
                                            ) *
                                            Number(
                                                meal.servings ||
                                                0
                                            )
                                        )}
                                        g
                              </span>

                                    </div>

                                  </div>

                                  <button
                                      className="delete-btn"
                                      onClick={() =>
                                          deleteMeal(
                                              meal.id
                                          )
                                      }
                                  >
                                    <Trash2
                                        size={14}
                                    />
                                    Delete
                                  </button>

                                </div>
                            );
                          })}

                        </div>
                    )}

                  </section>

                  {/* NUTRITION SUMMARY */}

                  <div
                      className="stats-grid"
                      style={{
                        marginTop: 22,
                      }}
                  >

                    <StatCard
                        icon={
                          <Flame size={24} />
                        }
                        iconClass="orange"
                        label="Calories"
                        value={formatNumber(
                            consumedCalories
                        )}
                        suffix="kcal"
                    />

                    <StatCard
                        icon={
                          <Dumbbell size={24} />
                        }
                        iconClass="blue"
                        label="Protein"
                        value={formatNumber(
                            consumedProtein
                        )}
                        suffix="g"
                    />

                    <StatCard
                        icon={
                          <Wheat size={24} />
                        }
                        iconClass="yellow"
                        label="Carbohydrates"
                        value={formatNumber(
                            consumedCarbs
                        )}
                        suffix="g"
                    />

                    <StatCard
                        icon={
                          <Leaf size={24} />
                        }
                        iconClass="green"
                        label="Fat"
                        value={formatNumber(
                            consumedFat
                        )}
                        suffix="g"
                    />

                  </div>

                </>
            )}

            {/* =================================================
              RECOMMENDATIONS
              ================================================= */}

            {page === "recommendations" && (
                <>

                  <div className="page-title">

                    <div>
                      <div className="page-label">
                        PERSONALIZED NUTRITION
                      </div>

                      <h1>
                        Recommendations
                      </h1>
                    </div>

                  </div>

                  <section className="panel">

                    <div className="panel-header">

                      <div>
                        <h2>
                          Foods Recommended
                          For You
                        </h2>

                        <p>
                          Based on your diet
                          type and nutrition
                          goal
                        </p>
                      </div>

                    </div>

                    {visibleRecommendations.length ===
                    0 ? (
                        <EmptyState
                            icon={
                              <Lightbulb size={35} />
                            }
                            message="No recommendations available."
                        />
                    ) : (
                        <div className="recommendation-grid">

                          {visibleRecommendations.map(
                              (food) => (

                                  <div
                                      className="food-card"
                                      key={food.id}
                                  >

                                    <div className="big-food-icon">
                                      {foodEmoji(
                                          food.name
                                      )}
                                    </div>

                                    <h3>
                                      {food.name}
                                    </h3>

                                    <div className="food-calories">
                                      {formatNumber(
                                          food.calories,
                                          0
                                      )}{" "}
                                      kcal / serving
                                    </div>

                                    <div className="food-macros">

                            <span>
                              Protein:{" "}
                              {formatNumber(
                                  food.proteinG
                              )}
                              g
                            </span>

                                      <span>
                              Carbs:{" "}
                                        {formatNumber(
                                            food.carbsG
                                        )}
                                        g
                            </span>

                                      <span>
                              Fat:{" "}
                                        {formatNumber(
                                            food.fatG
                                        )}
                                        g
                            </span>

                                    </div>

                                    <button
                                        className="primary-btn full"
                                        onClick={() =>
                                            openAddMeal(
                                                food
                                            )
                                        }
                                    >
                                      <Plus size={14} />
                                      Add to Today's Meals
                                    </button>

                                  </div>

                              )
                          )}

                        </div>
                    )}

                  </section>

                </>
            )}

          </section>

        </main>

        {/* =====================================================
          USER MODAL
          ===================================================== */}

        {showUserModal && (
            <div
                className="modal-overlay"
                onMouseDown={(event) => {
                  if (
                      event.target ===
                      event.currentTarget
                  ) {
                    setShowUserModal(false);
                  }
                }}
            >

              <div className="modal">

                <div className="modal-header">

                  <div>

                    <h2>
                      {editingUser
                          ? "Edit User"
                          : "Add User"}
                    </h2>

                    <p>
                      Create a personalized
                      nutrition profile
                    </p>

                  </div>

                  <button
                      className="close-btn"
                      onClick={() =>
                          setShowUserModal(false)
                      }
                  >
                    <X size={18} />
                  </button>

                </div>

                <form onSubmit={saveUser}>

                  <div className="form-grid">

                    <FormInput
                        label="Name"
                        value={userForm.name}
                        onChange={(value) =>
                            setUserForm({
                              ...userForm,
                              name: value,
                            })
                        }
                        required
                    />

                    <FormInput
                        label="Email"
                        type="email"
                        value={userForm.email}
                        onChange={(value) =>
                            setUserForm({
                              ...userForm,
                              email: value,
                            })
                        }
                        required
                    />

                    <FormInput
                        label="Age"
                        type="number"
                        value={userForm.age}
                        onChange={(value) =>
                            setUserForm({
                              ...userForm,
                              age: value,
                            })
                        }
                        required
                    />

                    <FormSelect
                        label="Gender"
                        value={userForm.gender}
                        onChange={(value) =>
                            setUserForm({
                              ...userForm,
                              gender: value,
                            })
                        }
                        options={[
                          ["MALE", "Male"],
                          ["FEMALE", "Female"],
                        ]}
                    />

                    <FormInput
                        label="Height (cm)"
                        type="number"
                        step="0.1"
                        value={userForm.heightCm}
                        onChange={(value) =>
                            setUserForm({
                              ...userForm,
                              heightCm: value,
                            })
                        }
                        required
                    />

                    <FormInput
                        label="Weight (kg)"
                        type="number"
                        step="0.1"
                        value={userForm.weightKg}
                        onChange={(value) =>
                            setUserForm({
                              ...userForm,
                              weightKg: value,
                            })
                        }
                        required
                    />

                    <FormSelect
                        label="Activity Level"
                        value={
                          userForm.activityLevel
                        }
                        onChange={(value) =>
                            setUserForm({
                              ...userForm,
                              activityLevel:
                              value,
                            })
                        }
                        options={[
                          [
                            "SEDENTARY",
                            "Sedentary",
                          ],
                          [
                            "LIGHT",
                            "Light",
                          ],
                          [
                            "MODERATE",
                            "Moderate",
                          ],
                          [
                            "ACTIVE",
                            "Active",
                          ],
                        ]}
                    />

                    <FormSelect
                        label="Goal"
                        value={userForm.goal}
                        onChange={(value) =>
                            setUserForm({
                              ...userForm,
                              goal: value,
                            })
                        }
                        options={[
                          [
                            "LOSE",
                            "Lose Weight",
                          ],
                          [
                            "MAINTAIN",
                            "Maintain Weight",
                          ],
                          [
                            "GAIN",
                            "Gain Weight",
                          ],
                        ]}
                    />

                    <FormSelect
                        label="Diet Type"
                        value={userForm.dietType}
                        onChange={(value) =>
                            setUserForm({
                              ...userForm,
                              dietType: value,
                            })
                        }
                        options={[
                          ["VEG", "Vegetarian"],
                          [
                            "NON_VEG",
                            "Non-Vegetarian",
                          ],
                        ]}
                    />

                  </div>

                  <div className="modal-actions">

                    <button
                        type="button"
                        className="secondary-btn"
                        onClick={() =>
                            setShowUserModal(false)
                        }
                    >
                      Cancel
                    </button>

                    <button
                        type="submit"
                        className="primary-btn"
                    >
                      <Check size={15} />

                      {editingUser
                          ? "Update User"
                          : "Create User"}
                    </button>

                  </div>

                </form>

              </div>

            </div>
        )}

        {/* =====================================================
          FOOD MODAL
          ===================================================== */}

        {showFoodModal && (
            <div
                className="modal-overlay"
                onMouseDown={(event) => {
                  if (
                      event.target ===
                      event.currentTarget
                  ) {
                    setShowFoodModal(false);
                  }
                }}
            >

              <div className="modal">

                <div className="modal-header">

                  <div>

                    <h2>
                      {editingFood
                          ? "Edit Food"
                          : "Add Food"}
                    </h2>

                    <p>
                      Enter nutritional
                      information per serving
                    </p>

                  </div>

                  <button
                      className="close-btn"
                      onClick={() =>
                          setShowFoodModal(false)
                      }
                  >
                    <X size={18} />
                  </button>

                </div>

                <form onSubmit={saveFood}>

                  <div className="form-grid">

                    <FormInput
                        label="Food Name"
                        value={foodForm.name}
                        onChange={(value) =>
                            setFoodForm({
                              ...foodForm,
                              name: value,
                            })
                        }
                        required
                    />

                    <FormSelect
                        label="Diet Type"
                        value={foodForm.dietType}
                        onChange={(value) =>
                            setFoodForm({
                              ...foodForm,
                              dietType: value,
                            })
                        }
                        options={[
                          ["VEG", "Vegetarian"],
                          [
                            "NON_VEG",
                            "Non-Vegetarian",
                          ],
                        ]}
                    />

                    <FormInput
                        label="Calories"
                        type="number"
                        step="0.1"
                        value={foodForm.calories}
                        onChange={(value) =>
                            setFoodForm({
                              ...foodForm,
                              calories: value,
                            })
                        }
                        required
                    />

                    <FormInput
                        label="Protein (g)"
                        type="number"
                        step="0.1"
                        value={foodForm.proteinG}
                        onChange={(value) =>
                            setFoodForm({
                              ...foodForm,
                              proteinG: value,
                            })
                        }
                        required
                    />

                    <FormInput
                        label="Carbohydrates (g)"
                        type="number"
                        step="0.1"
                        value={foodForm.carbsG}
                        onChange={(value) =>
                            setFoodForm({
                              ...foodForm,
                              carbsG: value,
                            })
                        }
                        required
                    />

                    <FormInput
                        label="Fat (g)"
                        type="number"
                        step="0.1"
                        value={foodForm.fatG}
                        onChange={(value) =>
                            setFoodForm({
                              ...foodForm,
                              fatG: value,
                            })
                        }
                        required
                    />

                  </div>

                  <div className="modal-actions">

                    <button
                        type="button"
                        className="secondary-btn"
                        onClick={() =>
                            setShowFoodModal(false)
                        }
                    >
                      Cancel
                    </button>

                    <button
                        type="submit"
                        className="primary-btn"
                    >
                      <Check size={15} />

                      {editingFood
                          ? "Update Food"
                          : "Create Food"}
                    </button>

                  </div>

                </form>

              </div>

            </div>
        )}

        {/* =====================================================
          MEAL MODAL
          ===================================================== */}

        {showMealModal && (
            <div
                className="modal-overlay"
                onMouseDown={(event) => {
                  if (
                      event.target ===
                      event.currentTarget
                  ) {
                    setShowMealModal(false);
                  }
                }}
            >

              <div className="modal small-modal">

                <div className="modal-header">

                  <div>

                    <h2>
                      Add Meal
                    </h2>

                    <p>
                      Log food for today
                    </p>

                  </div>

                  <button
                      className="close-btn"
                      onClick={() =>
                          setShowMealModal(false)
                      }
                  >
                    <X size={18} />
                  </button>

                </div>

                <form onSubmit={addMeal}>

                  <div className="form-grid">

                    <FormSelect
                        label="Food"
                        value={mealForm.foodId}
                        onChange={(value) =>
                            setMealForm({
                              ...mealForm,
                              foodId: value,
                            })
                        }
                        options={[
                          ["", "Select food"],
                          ...foods.map((food) => [
                            String(food.id),
                            `${food.name} - ${food.calories} kcal`,
                          ]),
                        ]}
                    />

                    <FormInput
                        label="Servings"
                        type="number"
                        min="0.5"
                        step="0.5"
                        value={mealForm.servings}
                        onChange={(value) =>
                            setMealForm({
                              ...mealForm,
                              servings: value,
                            })
                        }
                        required
                    />

                  </div>

                  <div className="modal-actions">

                    <button
                        type="button"
                        className="secondary-btn"
                        onClick={() =>
                            setShowMealModal(false)
                        }
                    >
                      Cancel
                    </button>

                    <button
                        type="submit"
                        className="primary-btn"
                    >
                      <Plus size={15} />
                      Add Meal
                    </button>

                  </div>

                </form>

              </div>

            </div>
        )}

      </div>
  );
}

/* =========================================================
   STAT CARD COMPONENT
   ========================================================= */

function StatCard({
                    icon,
                    iconClass,
                    label,
                    value,
                    suffix,
                  }) {
  return (
      <div className="stat-card">

        <div
            className={`stat-icon ${iconClass}`}
        >
          {icon}
        </div>

        <div>

          <div className="stat-label">
            {label}
          </div>

          <div className="stat-value">

            {value}

            <span>
            {suffix}
          </span>

          </div>

        </div>

      </div>
  );
}

/* =========================================================
   EMPTY STATE
   ========================================================= */

function EmptyState({
                      icon,
                      message,
                    }) {
  return (
      <div className="empty">

        {icon}

        <p>{message}</p>

      </div>
  );
}

/* =========================================================
   FORM INPUT
   ========================================================= */

function FormInput({
                     label,
                     type = "text",
                     value,
                     onChange,
                     required = false,
                     step,
                     min,
                   }) {
  return (
      <div className="form-group">

        <label>
          {label}
        </label>

        <input
            type={type}
            value={value}
            onChange={(event) =>
                onChange(event.target.value)
            }
            required={required}
            step={step}
            min={min}
        />

      </div>
  );
}

/* =========================================================
   FORM SELECT
   ========================================================= */

function FormSelect({
                      label,
                      value,
                      onChange,
                      options,
                    }) {
  return (
      <div className="form-group">

        <label>
          {label}
        </label>

        <select
            value={value}
            onChange={(event) =>
                onChange(event.target.value)
            }
        >

          {options.map(
              ([optionValue, optionLabel]) => (
                  <option
                      key={optionValue}
                      value={optionValue}
                  >
                    {optionLabel}
                  </option>
              )
          )}

        </select>

      </div>
  );
}

export default App;