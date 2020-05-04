package Main.model.requests;

import Main.model.Comment;
import Main.model.CommentStatus;
import Main.model.Off;

public class AddCommentRequest extends Request {
    private Comment comment;
    private final String name;

    public AddCommentRequest(Off off, String name) {
        this.comment = comment;
        this.name = name;
    }

    public String showRequest() {
        String show = "Add New Comment Request:\n" +
                "Request ID: " + this.requestId + "\n" +
                "Comment is related to product " + comment.getProduct().getName() + "\n" +
                "is written by user " + comment.getUser().getFirstName() + " " + comment.getUser().getLastName() + "\n" +
                "Comment Title: " + comment.getTitle() + "\n" +
                "Comment Content: " + comment.getContent();
        return show;
    }

    public void acceptRequest() {
        comment.getProduct().addComment(comment);
        comment.setCommentStatus(CommentStatus.APPROVED_COMMENT);
    }

    public void declineRequest() {
        comment.setCommentStatus(CommentStatus.DECLINED_COMMENT);
    }
}