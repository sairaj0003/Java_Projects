import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Movie {
    int id;
    String title;
    String genre;
    int duration;

    public Movie(int id, String title, String genre, int duration) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
    }
}

class Showtime {
    Movie movie;
    String time;
    List<List<Boolean>> seatMatrix;

    public Showtime(Movie movie, String time, int rows, int cols) {
        this.movie = movie;
        this.time = time;
        this.seatMatrix = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<Boolean> row = new ArrayList<>(cols);
            for (int j = 0; j < cols; j++) {
                row.add(false);  // Initialize all seats as unbooked
            }
            seatMatrix.add(row);
        }
    }
}

class Booking {
    static int nextBookingId = 1;
    int bookingId;
    Showtime showtime;
    int row;
    int col;

    public Booking(Showtime showtime, int row, int col) {
        this.bookingId = nextBookingId++;
        this.showtime = showtime;
        this.row = row;
        this.col = col;
    }
}

public class OnlineMovieTicketBookingSystem {
    static Map<Integer, Movie> movies = new HashMap<>();
    static List<Showtime> showtimes = new ArrayList<>();
    static List<Booking> bookings = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeMovies();
        initializeShowtimes();

        while (true) {
            System.out.println("1. Browse Movies and Book Tickets");
            System.out.println("2. View Showtimes");
            System.out.println("3. View Bookings");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    browseMoviesAndBookTickets();
                    break;
                case 2:
                    viewShowtimes();
                    break;
                case 3:
                    viewBookings();
                    break;
                case 4:
                    cancelBooking();
                    break;
                case 5:
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    static void initializeMovies() {
        movies.put(1, new Movie(1, "Inception", "Sci-Fi", 150));
        movies.put(2, new Movie(2, "The Shawshank Redemption", "Drama", 142));
        movies.put(3, new Movie(3, "The Dark Knight", "Action", 152));
        movies.put(4, new Movie(4, "Pulp Fiction", "Crime", 154));
        movies.put(5, new Movie(5, "Forrest Gump", "Drama", 142));
        movies.put(6, new Movie(6, "The Matrix", "Action", 136));
    }

    static void initializeShowtimes() {
        for (Movie movie : movies.values()) {
            showtimes.add(new Showtime(movie, "12:00 PM", 5, 10));
            showtimes.add(new Showtime(movie, "03:00 PM", 5, 10));
            showtimes.add(new Showtime(movie, "06:00 PM", 5, 10));
        }
    }

    static void browseMoviesAndBookTickets() {
        System.out.println("Available Movies:");
        for (Movie movie : movies.values()) {
            System.out.println(movie.id + ". " + movie.title);
        }

        System.out.print("Enter the movie ID: ");
        int movieId = scanner.nextInt();

        Movie selectedMovie = movies.get(movieId);

        if (selectedMovie != null) {
            System.out.println("Showtimes for " + selectedMovie.title + ":");
            int showtimeNumber = 1;
            for (Showtime showtime : showtimes) {
                if (showtime.movie.id == movieId) {
                    System.out.println(showtimeNumber + ". " + showtime.time);
                    showtimeNumber++;
                }
            }

            System.out.print("Enter the showtime number: ");
            int selectedShowtimeNumber = scanner.nextInt();

            Showtime selectedShowtime = findShowtime(movieId, selectedShowtimeNumber);

            if (selectedShowtime != null) {
                bookTickets(selectedShowtime);
            } else {
                System.out.println("Invalid showtime number.");
            }
        } else {
            System.out.println("Invalid movie ID.");
        }
    }

    static Showtime findShowtime(int movieId, int showtimeNumber) {
        int count = 1;
        for (Showtime showtime : showtimes) {
            if (showtime.movie.id == movieId) {
                if (count == showtimeNumber) {
                    return showtime;
                }
                count++;
            }
        }
        return null;
    }

    static void bookTickets(Showtime showtime) {
        System.out.println("Seat Matrix:");
        for (int i = 0; i < showtime.seatMatrix.size(); i++) {
            List<Boolean> row = showtime.seatMatrix.get(i);
            for (int j = 0; j < row.size(); j++) {
                System.out.print(row.get(j) ? "X" : "O");
            }
            System.out.println();
        }

        System.out.print("Enter the row number: ");
        int selectedRow = scanner.nextInt();
        System.out.print("Enter the seat number: ");
        int selectedSeat = scanner.nextInt();

        if (selectedRow > 0 && selectedRow <= showtime.seatMatrix.size() &&
                selectedSeat > 0 && selectedSeat <= showtime.seatMatrix.get(0).size()) {
            if (!showtime.seatMatrix.get(selectedRow - 1).get(selectedSeat - 1)) {
                showtime.seatMatrix.get(selectedRow - 1).set(selectedSeat - 1, true);
                Booking booking = new Booking(showtime, selectedRow, selectedSeat);
                bookings.add(booking);
                System.out.println("Ticket booked successfully. Your booking ID is: " + booking.bookingId);
            } else {
                System.out.println("Seat already booked. Please choose another seat.");
            }
        } else {
            System.out.println("Invalid row or seat number.");
        }
    }

    static void viewShowtimes() {
        System.out.println("Showtimes:");
        for (Showtime showtime : showtimes) {
            System.out.println(showtime.movie.id + ". " + showtime.movie.title +
                    " - " + showtime.time);
        }
    }

    static void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings to display.");
            return;
        }

        System.out.println("Your Bookings:");
        for (Booking booking : bookings) {
            System.out.println("Booking ID: " + booking.bookingId +
                    " | " + booking.showtime.movie.title + " - " + booking.showtime.time +
                    " | Row: " + booking.row + " | Seat: " + booking.col);
        }
    }

    static void cancelBooking() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings to cancel.");
            return;
        }

        System.out.print("Enter the booking ID to cancel: ");
        int bookingIdToCancel = scanner.nextInt();

        Booking bookingToRemove = findBookingById(bookingIdToCancel);
        if (bookingToRemove != null) {
            bookings.remove(bookingToRemove);
            markSeatAsUnbooked(bookingToRemove.showtime, bookingToRemove.row, bookingToRemove.col);
            System.out.println("Booking canceled successfully.");
        } else {
            System.out.println("Booking not found with the provided ID.");
        }
    }

    static Booking findBookingById(int bookingId) {
        for (Booking booking : bookings) {
            if (booking.bookingId == bookingId) {
                return booking;
            }
        }
        return null;
    }

    static void markSeatAsUnbooked(Showtime showtime, int row, int seat) {
        showtime.seatMatrix.get(row - 1).set(seat - 1, false);
    }
}
